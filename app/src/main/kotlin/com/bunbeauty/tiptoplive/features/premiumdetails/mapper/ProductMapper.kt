package com.bunbeauty.tiptoplive.features.premiumdetails.mapper

import com.bunbeauty.tiptoplive.features.billing.model.Product
import com.bunbeauty.tiptoplive.features.premiumdetails.view.SubscriptionItem

fun List<Product>.toSubscriptions(): List<SubscriptionItem> {
    return mapIndexed { index, product ->
        SubscriptionItem(
            id = product.id,
            offerToken = product.offerToken,
            name = product.name,
            currentPrice = product.currentPrice,
            previousPrice = product.previousPrice,
            discountPercent = "${product.discountPercent}%",
            isLifetime = product.id == "lifetime",
            isSelected = index == 0
        )
    }
}