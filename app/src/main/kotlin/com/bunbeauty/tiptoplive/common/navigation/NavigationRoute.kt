package com.bunbeauty.tiptoplive.common.navigation

import kotlinx.serialization.Serializable

sealed interface BottomNavigationRoute: NavigationRoute

interface NavigationRoute {

    @Serializable
    data object Intro: NavigationRoute

    @Serializable
    data object Preparation: BottomNavigationRoute

    @Serializable
    data object Progress: BottomNavigationRoute

    @Serializable
    data object More: BottomNavigationRoute

    @Serializable
    data class CropImage(val uri: String): NavigationRoute

    @Serializable
    data object Stream: NavigationRoute

    @Serializable
    data object PremiumDetails: NavigationRoute

    @Serializable
    data object SuccessfullyPurchased: NavigationRoute

    @Serializable
    data object PurchaseFailed: NavigationRoute

}