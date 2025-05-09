package com.bunbeauty.tiptoplive.features.premiumdetails.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.features.billing.BillingService
import com.bunbeauty.tiptoplive.features.billing.PurchaseResultListener
import com.bunbeauty.tiptoplive.features.billing.domain.IsPremiumAvailableUseCase
import com.bunbeauty.tiptoplive.features.billing.model.PurchaseData
import com.bunbeauty.tiptoplive.features.billing.model.PurchaseResult
import com.bunbeauty.tiptoplive.features.premiumdetails.domain.GetOfferTimerFlowUseCase
import com.bunbeauty.tiptoplive.features.premiumdetails.mapper.toSubscriptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PremiumDetailsViewModel @Inject constructor(
    private val analyticsManager: AnalyticsManager,
    private val billingService: BillingService,
    private val purchaseResultListener: PurchaseResultListener,
    private val getOfferTimerFlowUseCase: GetOfferTimerFlowUseCase,
    private val isPremiumAvailableUseCase: IsPremiumAvailableUseCase
) : BaseViewModel<PremiumDetails.State, PremiumDetails.Action, PremiumDetails.Event>(
    initState = {
        PremiumDetails.State(
            freePlan = PremiumDetails.Plan(
                isSelected = false,
                isCurrent = false,
                subscriptions = emptyList()
            ),
            premiumPlan = PremiumDetails.Plan(
                isSelected = true,
                isCurrent = false,
                subscriptions = emptyList()
            ),
            timer = null,
            isCrossIconVisible = false
        )
    }
) {

    init {
        checkPremium()
        showCrossIcon()
    }

    override fun onAction(action: PremiumDetails.Action) {
        when (action) {
            PremiumDetails.Action.CloseClicked -> {
                analyticsManager.trackPremiumQuite()
                sendEvent(PremiumDetails.Event.NavigateBack)
            }

            is PremiumDetails.Action.SelectPlan -> {
                setState {
                    copy(
                        freePlan = freePlan.copy(isSelected = action.index == 0),
                        premiumPlan = premiumPlan.copy(isSelected = action.index == 1)
                    )
                }
            }

            is PremiumDetails.Action.SubscriptionClick -> {
                setState {
                    copy(
                        premiumPlan = premiumPlan.copy(
                            subscriptions = premiumPlan.subscriptions.map { subscription ->
                                subscription.copy(isSelected = subscription.id == action.id)
                            }
                        )
                    )
                }
            }

            PremiumDetails.Action.CheckoutClick -> {
                currentState.selectedSubscription?.let { subscription ->
                    analyticsManager.trackCheckoutClick(productId = subscription.id)
                    sendEvent(
                        PremiumDetails.Event.StartCheckout(
                            purchaseData = PurchaseData(
                                productId = subscription.id,
                                offerToken = subscription.offerToken
                            )
                        )
                    )
                }
            }
        }
    }

    private fun checkPremium() {
        viewModelScope.launch {
            val isPremium = isPremiumAvailableUseCase()
            if (!isPremium) {
                loadSubscriptions()
            }

            setState {
                copy(
                    freePlan = freePlan.copy(isCurrent = !isPremium),
                    premiumPlan = premiumPlan.copy(
                        isCurrent = isPremium,
                        subscriptions = if (isPremium) {
                            emptyList()
                        } else {
                            premiumPlan.subscriptions
                        }
                    )
                )
            }
        }
    }

    private fun loadSubscriptions() {
        viewModelScope.launch {
            val subscriptions = billingService.getProducts(
                listOf("monthly", "lifetime")
            ).toSubscriptions()
            setState {
                copy(
                    premiumPlan = premiumPlan.copy(
                        subscriptions = subscriptions
                    )
                )
            }

            if (subscriptions.isNotEmpty()) {
                startOfferTimer()
                subscribeOnPurchaseFlow()
            }
        }
    }

    private fun showCrossIcon() {
        viewModelScope.launch {
            delay(1_500)
            setState {
                copy(isCrossIconVisible = true)
            }
        }
    }

    private fun startOfferTimer() {
        getOfferTimerFlowUseCase().onEach { timer ->
            setState {
                copy(timer = timer)
            }
        }.launchIn(viewModelScope)
    }

    private fun subscribeOnPurchaseFlow() {
        purchaseResultListener.purchaseResult.onEach { result ->
            when (result) {
                is PurchaseResult.Success -> {
                    billingService.acknowledgePurchase(purchasedProduct = result.product)
                        .onSuccess {
                            sendEvent(PremiumDetails.Event.NavigateToPurchaseSuccess)
                        }.onError {
                            handleError()
                        }
                }

                is PurchaseResult.Error -> {
                    handleError()
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun handleError() {
        sendEvent(PremiumDetails.Event.NavigateToPurchaseFailed)
    }

}