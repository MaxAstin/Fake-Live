package com.bunbeauty.tiptoplive.common.navigation

import kotlinx.serialization.Serializable

interface NavigationRote {

    @Serializable
    data object Intro

    @Serializable
    data object Preparation

    @Serializable
    data object Progress

    @Serializable
    data class CropImage(val uri: String)

    @Serializable
    data object Stream

    @Serializable
    data object Subscription

    @Serializable
    data object PremiumDetails

    @Serializable
    data object SuccessfullyPurchased

    @Serializable
    data object PurchaseFailed

}