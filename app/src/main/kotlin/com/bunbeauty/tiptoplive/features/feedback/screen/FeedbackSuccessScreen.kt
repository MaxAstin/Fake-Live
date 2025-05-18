package com.bunbeauty.tiptoplive.features.feedback.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.screen.SuccessScreen

@Composable
fun FeedbackSuccessScreen(navController: NavHostController) {
    SuccessScreen(
        title = stringResource(R.string.feedback_thank_you_title),
        description = stringResource(R.string.feedback_thank_you_description),
        mainButtonText = stringResource(R.string.feedback_done),
        onMainButtonClick = {
            navController.popBackStack()
        },
        onCloseIconClick = {
            navController.popBackStack()
        }
    )
}

@LocalePreview
@Composable
private fun FeedbackSuccessScreenPreview() {
    FeedbackSuccessScreen(navController = rememberNavController())
}