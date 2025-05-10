package com.bunbeauty.tiptoplive.features.premiumdetails.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.navigation.NavigationRoute
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.screen.SuccessScreen
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

@Composable
fun PurchaseSuccessScreen(navController: NavHostController) {
    SuccessScreen(
        title = stringResource(R.string.subscription_success_title),
        description = stringResource(R.string.subscription_success_description),
        mainButtonText = stringResource(R.string.subscription_success_start_using),
        onMainButtonClick = {
            navController.navigate(NavigationRoute.Preparation) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        },
        onCloseIconOnClick = {
            navController.popBackStack()
        }
    )
}

@LocalePreview
@Composable
private fun SuccessfullyPurchasedScreenPreview() {
    FakeLiveTheme {
        PurchaseSuccessScreen(
            navController = rememberNavController()
        )
    }
}