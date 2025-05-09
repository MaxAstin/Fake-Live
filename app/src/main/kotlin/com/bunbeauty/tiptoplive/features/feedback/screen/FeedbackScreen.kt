package com.bunbeauty.tiptoplive.features.feedback.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.navigation.NavigationRoute
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.components.CloseIcon
import com.bunbeauty.tiptoplive.common.ui.components.FakeLiveTextField
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLivePrimaryButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.util.showToast
import com.bunbeauty.tiptoplive.features.feedback.presentation.Feedback
import com.bunbeauty.tiptoplive.features.feedback.presentation.FeedbackViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun FeedbackScreen(navController: NavHostController) {
    val viewModel: FeedbackViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { action: Feedback.Action ->
            viewModel.onAction(action)
        }
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.event.onEach { event ->
            when (event) {
                Feedback.Event.NavigateBack -> {
                    navController.navigateUp()
                }

                Feedback.Event.NavigateToSuccess -> {
                    navController.navigate(NavigationRoute.FeedbackSuccess) {
                        popUpTo<NavigationRoute.Feedback> {
                            inclusive = true
                        }
                    }
                }

                Feedback.Event.ShowSendingFailed -> {
                    context.apply {
                        showToast(message = getString(R.string.common_something_went_wrong))
                    }
                }
            }
        }.launchIn(this)
    }

    FeedbackScreenContent(
        state = state,
        onAction = onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeedbackScreenContent(
    state: Feedback.State,
    onAction: (Feedback.Action) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FakeLiveTheme.colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.feedback_title),
                        color = FakeLiveTheme.colors.onBackground,
                        style = FakeLiveTheme.typography.titleLarge
                    )
                },
                actions = {
                    CloseIcon(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            onAction(Feedback.Action.CloseClick)
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FakeLiveTheme.colors.background,
                    navigationIconContentColor = FakeLiveTheme.colors.onBackground
                )
            )
        },
        floatingActionButton = {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                if (state.sending) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.Center),
                        color = FakeLiveTheme.colors.interactive
                    )
                } else {
                    val focusManager = LocalFocusManager.current
                    FakeLivePrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.feedback_send),
                        enabled = state.feedback.isNotBlank(),
                        onClick = {
                            focusManager.clearFocus()
                            onAction(Feedback.Action.SendClick)
                        }
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 72.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.feedback_description,
                    stringResource(R.string.app_name)
                ),
                color = FakeLiveTheme.colors.onBackground,
                style = FakeLiveTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.weight(1f))

            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            FakeLiveTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = state.feedback,
                hint = stringResource(R.string.feedback_what_to_improve),
                readOnly = false,
                singleLine = false,
                onValueChange = { value ->
                    onAction(Feedback.Action.UpdateFeedback(text = value))
                }
            )
        }
    }
}

@Composable
@LocalePreview
private fun FeedbackScreenPreview() {
    FakeLiveTheme {
        FeedbackScreenContent(
            state = Feedback.State(
                feedback = "feedback",
                sending = false
            ),
            onAction = {}
        )
    }
}