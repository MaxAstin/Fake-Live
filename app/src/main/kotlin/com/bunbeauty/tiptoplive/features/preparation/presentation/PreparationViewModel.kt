package com.bunbeauty.tiptoplive.features.preparation.presentation

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.common.ui.components.ImageSource
import com.bunbeauty.tiptoplive.features.billing.domain.IsPremiumAvailableUseCase
import com.bunbeauty.tiptoplive.features.premiumdetails.domain.GetOfferTimerFlowUseCase
import com.bunbeauty.tiptoplive.features.preparation.domain.GetShowStreamDurationLimitUseCase
import com.bunbeauty.tiptoplive.features.preparation.domain.SaveNotifiedOfStreamDurationLimitUseCase
import com.bunbeauty.tiptoplive.features.preparation.domain.SaveShowStreamDurationLimitUseCase
import com.bunbeauty.tiptoplive.features.preparation.domain.SetupNotificationUseCase
import com.bunbeauty.tiptoplive.features.preparation.presentation.Preparation.ViewerCountItem
import com.bunbeauty.tiptoplive.shared.domain.GetImageUriFlowUseCase
import com.bunbeauty.tiptoplive.shared.domain.GetUsernameUseCase
import com.bunbeauty.tiptoplive.shared.domain.GetViewerCountUseCase
import com.bunbeauty.tiptoplive.shared.domain.SaveUsernameUseCase
import com.bunbeauty.tiptoplive.shared.domain.SaveViewerCountUseCase
import com.bunbeauty.tiptoplive.shared.domain.model.ViewerCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreparationViewModel @Inject constructor(
    private val getImageUriFlowUseCase: GetImageUriFlowUseCase,
    private val getShowStreamDurationLimitUseCase: GetShowStreamDurationLimitUseCase,
    private val saveShowStreamDurationLimitUseCase: SaveShowStreamDurationLimitUseCase,
    private val saveNotifiedOfStreamDurationLimitUseCase: SaveNotifiedOfStreamDurationLimitUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val saveUsernameUseCase: SaveUsernameUseCase,
    private val getViewerCountUseCase: GetViewerCountUseCase,
    private val saveViewerCountUseCase: SaveViewerCountUseCase,
    private val isPremiumAvailableUseCase: IsPremiumAvailableUseCase,
    private val setupNotificationUseCase: SetupNotificationUseCase,
    private val getOfferTimerFlowUseCase: GetOfferTimerFlowUseCase,
    private val analyticsManager: AnalyticsManager
) : BaseViewModel<Preparation.State, Preparation.Action, Preparation.Event>(
    initState = {
        Preparation.State(
            newLevel = false,
            image = ImageSource.ResId(R.drawable.img_default_avatar),
            username = "",
            viewerCountList = persistentListOf(),
            viewerCount = ViewerCount.V_100_200,
            premiumStatus = Preparation.PremiumStatus.Loading,
            showStreamDurationLimitsDialog = null
        )
    }
) {

    private var offerTimerJob: Job? = null

    init {
        initState()
        observeAndSaveUsername()
    }

    override fun onAction(action: Preparation.Action) {
        when (action) {
            Preparation.Action.StartScreen -> {
                checkPremiumStatus()
                checkShowStreamDurationLimit()
            }

            Preparation.Action.SetupNotification -> {
                setupNotificationUseCase()
            }

            Preparation.Action.ProgressClick -> {
                sendEvent(Preparation.Event.NavigateToProgress)
            }

            is Preparation.Action.ViewerCountSelect -> {
                if (action.item.isAvailable) {
                    viewModelScope.launch {
                        saveViewerCountUseCase(viewerCount = action.item.viewerCount)
                    }
                    mutableState.update { state ->
                        state.copy(viewerCount = action.item.viewerCount)
                    }
                } else {
                    sendEvent(Preparation.Event.NavigateToPremiumDetails)
                }
            }

            is Preparation.Action.UsernameUpdate -> {
                mutableState.update { state ->
                    state.copy(username = action.username)
                }
            }

            Preparation.Action.AvatarClick -> {
                sendEvent(Preparation.Event.HandleAvatarClick)
            }

            Preparation.Action.StartStreamClick -> {
                viewModelScope.launch {
                    analyticsManager.trackStreamStart(
                        username = mutableState.value.username,
                        viewerCount = mutableState.value.viewerCount.min
                    )
                    saveUsernameUseCase(mutableState.value.username)
                    sendEvent(Preparation.Event.OpenStream)
                }
            }

            Preparation.Action.CloseStreamDurationLimitsDialogClick -> {
                setState {
                    copy(showStreamDurationLimitsDialog = false)
                }
            }

            is Preparation.Action.PremiumLaterClick -> {
                analyticsManager.trackPremiumLaterClick()
                setState {
                    copy(showStreamDurationLimitsDialog = false)
                }
                viewModelScope.launch {
                    saveNotifiedOfStreamDurationLimitUseCase(notified = true)
                }
            }

            Preparation.Action.PremiumClick -> {
                setState { copy(showStreamDurationLimitsDialog = false) }
                viewModelScope.launch {
                    saveNotifiedOfStreamDurationLimitUseCase(notified = true)
                }
                analyticsManager.trackPremiumClick()
                sendEvent(Preparation.Event.NavigateToPremiumDetails)
            }
        }
    }

    private fun initState() {
        viewModelScope.launch {
            val usernameDeferred = async { getUsernameUseCase() }
            val viewerCountDeferred = async { getViewerCountUseCase() }
            val username = usernameDeferred.await()
            val viewerCount = viewerCountDeferred.await()

            setState {
                copy(
                    username = username,
                    viewerCount = viewerCount
                )
            }
        }
        getImageUriFlowUseCase().onEach { imageUri ->
            setState {
                copy(
                    image = if (imageUri == null) {
                        ImageSource.ResId(data = R.drawable.img_default_avatar)
                    } else {
                        ImageSource.Device(data = imageUri.toUri())
                    }
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun checkShowStreamDurationLimit() {
        viewModelScope.launch {
            if (getShowStreamDurationLimitUseCase()) {
                setState {
                    copy(showStreamDurationLimitsDialog = true)
                }
                saveShowStreamDurationLimitUseCase(show = false)
            }
        }
    }

    private fun checkPremiumStatus() {
        viewModelScope.launch {
            if (isPremiumAvailableUseCase()) {
                setState {
                    copy(premiumStatus = Preparation.PremiumStatus.Active)
                }
            } else {
                if (offerTimerJob == null) {
                    offerTimerJob = getOfferTimerFlowUseCase().onEach { offerTimer ->
                        setState {
                            copy(
                                premiumStatus = Preparation.PremiumStatus.Free(
                                    offerTimer = offerTimer
                                )
                            )
                        }
                    }.launchIn(this)
                }
            }
            setState {
                copy(
                    viewerCountList = ViewerCount.entries.map { viewerCount ->
                        ViewerCountItem(
                            viewerCount = viewerCount,
                            isAvailable = viewerCount == ViewerCount.V_100_200
                                || premiumStatus is Preparation.PremiumStatus.Active,
                        )
                    }.toImmutableList()
                )
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeAndSaveUsername() {
        mutableState.map { state ->
            state.username
        }.distinctUntilChanged()
            .debounce(1_000)
            .onEach { username ->
                saveUsernameUseCase(username)
            }.launchIn(viewModelScope)
    }

}