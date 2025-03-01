package com.bunbeauty.tiptoplive.features.subscription.presentation

import com.bunbeauty.tiptoplive.common.presentation.Base
import com.bunbeauty.tiptoplive.features.billing.model.PurchaseData
import com.bunbeauty.tiptoplive.features.subscription.view.SubscriptionItem

interface Subscription {

    data class State(
        val subscriptions: List<SubscriptionItem>,
        val timer: String?
    ): Base.State {

        val selectedSubscription: SubscriptionItem?
            get() = subscriptions.find { it.isSelected }

    }

    sealed interface Action: Base.Action {
        data object CloseClicked: Action
        data class SubscriptionClick(val id: String): Action
        data object CheckoutClick: Action
    }

    sealed interface Event: Base.Event {
        data object NavigateBack: Event
        data class StartCheckout(val purchaseData: PurchaseData): Event
        data object NavigateToPurchase: Event
        data object NavigateToPurchaseFailed: Event
    }

}