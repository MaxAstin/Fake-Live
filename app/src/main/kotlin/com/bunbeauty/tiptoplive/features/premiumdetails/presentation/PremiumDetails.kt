package com.bunbeauty.tiptoplive.features.premiumdetails.presentation

import com.bunbeauty.tiptoplive.common.presentation.Base
import com.bunbeauty.tiptoplive.features.billing.model.PurchaseData
import com.bunbeauty.tiptoplive.features.premiumdetails.view.SubscriptionItem

interface PremiumDetails {

    data class State(
        val freePlan: Plan,
        val premiumPlan: Plan,
        val timer: String?,
        val isCrossIconVisible: Boolean
    ) : Base.State {

        val isPremiumSelected: Boolean = premiumPlan.isSelected
        val isFreeSelected: Boolean = freePlan.isSelected
        val selectedIndex: Int = if (isPremiumSelected) 1 else 0
        val selectedPlan: Plan = if (premiumPlan.isSelected) {
            premiumPlan
        } else {
            freePlan
        }
        val selectedSubscription: SubscriptionItem? = premiumPlan.subscriptions.find { subscription ->
            subscription.isSelected
        }

    }

    data class Plan(
        val isSelected: Boolean,
        val isCurrent: Boolean,
        val subscriptions: List<SubscriptionItem>
    )

    sealed interface Action : Base.Action {
        data object CloseClicked : Action
        data class SelectPlan(val index: Int) : Action
        data class SubscriptionClick(val id: String) : Action
        data object CheckoutClick : Action
    }

    sealed interface Event : Base.Event {
        data object NavigateBack : Event
        data class StartCheckout(val purchaseData: PurchaseData) : Event
        data object NavigateToPurchaseSuccess : Event
        data object NavigateToPurchaseFailed : Event
    }

}