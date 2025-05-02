package com.bunbeauty.tiptoplive.features.more.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.features.billing.domain.IsPremiumAvailableUseCase
import com.bunbeauty.tiptoplive.features.premiumdetails.domain.GetOfferTimerFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val isPremiumAvailableUseCase: IsPremiumAvailableUseCase,
    private val getOfferTimerFlowUseCase: GetOfferTimerFlowUseCase,
    private val analyticsManager: AnalyticsManager
) : BaseViewModel<More.State, More.Action, More.Event>(
    initState = {
        More.State(premium = More.Premium.Loading)
    }
) {

    init {
        checkPremium()
    }

    override fun onAction(action: More.Action) {
        when (action) {
            More.Action.PremiumClick -> {
                analyticsManager.trackPremiumClick()
                sendEvent(More.Event.NavigateToPremiumDetails)
            }
            More.Action.ShareClick -> {
                analyticsManager.trackShare()
                sendEvent(More.Event.OpenSharing)
            }
        }
    }

    private fun checkPremium() {
        viewModelScope.launch {
            val isPremium = isPremiumAvailableUseCase()
            if (isPremium) {
                setState {
                    copy(premium = More.Premium.Active)
                }
            } else {
                getOfferTimerFlowUseCase().onEach { timer ->
                    setState {
                        copy(
                            premium = More.Premium.Discount(
                                timer = timer
                            )
                        )
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

}