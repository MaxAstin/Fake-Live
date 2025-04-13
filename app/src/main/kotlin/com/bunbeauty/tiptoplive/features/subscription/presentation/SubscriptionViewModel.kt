package com.bunbeauty.tiptoplive.features.subscription.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.features.billing.BillingService
import com.bunbeauty.tiptoplive.features.billing.PurchaseResultListener
import com.bunbeauty.tiptoplive.features.billing.domain.IsPremiumAvailableUseCase
import com.bunbeauty.tiptoplive.features.billing.model.PurchaseData
import com.bunbeauty.tiptoplive.features.billing.model.PurchaseResult
import com.bunbeauty.tiptoplive.features.subscription.domain.GetOfferTimerFlowUseCase
import com.bunbeauty.tiptoplive.features.subscription.mapper.toSubscriptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val analyticsManager: AnalyticsManager,
    private val billingService: BillingService,
    private val purchaseResultListener: PurchaseResultListener,
    private val getOfferTimerFlowUseCase: GetOfferTimerFlowUseCase,
    private val isPremiumAvailableUseCase: IsPremiumAvailableUseCase
) : BaseViewModel<Subscription.State, Subscription.Action, Subscription.Event>(
    initState = {
        Subscription.State(
            freePlan = Subscription.Plan(
                isSelected = false,
                isCurrent = false,
                subscriptions = emptyList()
            ),
            premiumPlan = Subscription.Plan(
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

    override fun onAction(action: Subscription.Action) {
        when (action) {
            Subscription.Action.CloseClicked -> {
                analyticsManager.trackPremiumQuite()
                sendEvent(Subscription.Event.NavigateBack)
            }

            is Subscription.Action.SelectPlan -> {
                setState {
                    copy(
                        freePlan = freePlan.copy(isSelected = action.index == 0),
                        premiumPlan = premiumPlan.copy(isSelected = action.index == 1)
                    )
                }
            }

            is Subscription.Action.SubscriptionClick -> {
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

            Subscription.Action.CheckoutClick -> {
                currentState.selectedSubscription?.let { subscription ->
                    analyticsManager.trackCheckoutClick(productId = subscription.id)
                    sendEvent(
                        Subscription.Event.StartCheckout(
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
                            sendEvent(Subscription.Event.NavigateToPurchase)
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
        sendEvent(Subscription.Event.NavigateToPurchaseFailed)
    }

}