package com.bunbeauty.tiptoplive.features.main

import android.Manifest.permission.CAMERA
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.Constants.NOTIFICATION_MESSAGE_KEY
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.navigation.NavigationRoute
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.util.launchInAppReview
import com.bunbeauty.tiptoplive.common.util.openSettings
import com.bunbeauty.tiptoplive.common.util.serializable
import com.bunbeauty.tiptoplive.common.util.showToast
import com.bunbeauty.tiptoplive.features.billing.BillingService
import com.bunbeauty.tiptoplive.features.billing.model.PurchaseData
import com.bunbeauty.tiptoplive.features.cropimage.view.CropImageScreen
import com.bunbeauty.tiptoplive.features.feedback.screen.FeedbackScreen
import com.bunbeauty.tiptoplive.features.feedback.screen.FeedbackSuccessScreen
import com.bunbeauty.tiptoplive.features.intro.view.IntroScreen
import com.bunbeauty.tiptoplive.features.main.presentation.Main
import com.bunbeauty.tiptoplive.features.main.presentation.MainViewModel
import com.bunbeauty.tiptoplive.features.main.view.BottomNavigationBar
import com.bunbeauty.tiptoplive.features.main.view.CameraIsRequiredDialog
import com.bunbeauty.tiptoplive.features.more.MoreScreen
import com.bunbeauty.tiptoplive.features.notification.NotificationMessage
import com.bunbeauty.tiptoplive.features.premiumdetails.view.PremiumDetailsScreen
import com.bunbeauty.tiptoplive.features.premiumdetails.view.PurchaseFailedScreen
import com.bunbeauty.tiptoplive.features.premiumdetails.view.PurchaseSuccessScreen
import com.bunbeauty.tiptoplive.features.preparation.view.PreparationScreen
import com.bunbeauty.tiptoplive.features.progress.AwardsScreen
import com.bunbeauty.tiptoplive.features.stream.view.StreamScreen
import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var billingService: Lazy<BillingService>

    @Inject
    lateinit var analyticsManager: Lazy<AnalyticsManager>

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            mainViewModel.onAction(Main.Action.CameraPermissionAccept)
        } else {
            mainViewModel.onAction(Main.Action.CameraPermissionDeny)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        intent.extras?.serializable<NotificationMessage>(NOTIFICATION_MESSAGE_KEY)
            ?.let { notificationMessage ->
                mainViewModel.onAction(
                    Main.Action.NotificationClick(
                        notificationMessage = notificationMessage
                    )
                )
            }

        setContent {
            FakeLiveTheme {
                val state by mainViewModel.state.collectAsStateWithLifecycle()

                AppContent(state = state)

                val onAction = remember {
                    { action: Main.Action ->
                        mainViewModel.onAction(action)
                    }
                }
                if (state.showNoCameraPermission) {
                    CameraIsRequiredDialog(
                        onAction = onAction,
                        onSettingsClick = {
                            openSettings()
                        }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.onAction(Main.Action.AppStart)
    }


    override fun onStop() {
        super.onStop()
        mainViewModel.onAction(Main.Action.AppStop)
    }

    private fun requestCameraPermission() {
        val isCameraPermissionDenied = ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA)
        if (isCameraPermissionDenied) {
            mainViewModel.onAction(Main.Action.CameraPermissionDeny)
        } else {
            requestCameraPermissionLauncher.launch(CAMERA)
        }
    }

    @Composable
    private fun AppContent(state: Main.State) {
        val navController = rememberNavController()
        Scaffold(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding(),
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    hasNewAwards = state.hasNewAwards
                )
            }
        ) { padding ->
            LaunchedEffect(Unit) {
                mainViewModel.event.onEach { event ->
                    when (event) {
                        Main.Event.OpenStream -> {
                            navController.navigate(NavigationRoute.Stream)
                        }
                    }
                }.launchIn(this)
            }

            MainNavigation(
                navController = navController,
                modifier = Modifier.padding(padding)
            )
        }
    }

    @Composable
    private fun MainNavigation(
        navController: NavHostController,
        modifier: Modifier = Modifier,
    ) {
        NavHost(
            navController = navController,
            startDestination = NavigationRoute.Intro,
            modifier = modifier,
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                ExitTransition.None
            },
        ) {
            composable<NavigationRoute.Intro> {
                IntroScreen(navController = navController)
            }
            composable<NavigationRoute.Preparation> {
                PreparationScreen(
                    navController = navController,
                    onStartStreamClick = {
                        requestCameraPermission()
                    },
                    onPositiveFeedbackClick = {
                        launchInAppReview()
                    }
                )
            }
            composable<NavigationRoute.Awards> {
                AwardsScreen()
            }
            composable<NavigationRoute.Stream> {
                StreamScreen(navController = navController)
            }
            composable<NavigationRoute.More> {
                MoreScreen(navController = navController)
            }
            composable<NavigationRoute.CropImage> { navBackStackEntry ->
                val cropImageRoute: NavigationRoute.CropImage = navBackStackEntry.toRoute()
                val uri = cropImageRoute.uri.toUri()
                CropImageScreen(
                    navController = navController,
                    uri = uri,
                    onMockClick = {
                        showToast(message = getString(R.string.common_under_development))
                    }
                )
            }
            composable<NavigationRoute.PremiumDetails> {
                PremiumDetailsScreen(
                    navController = navController,
                    startCheckout = ::startCheckout
                )
            }
            composable<NavigationRoute.PurchaseSuccess> {
                PurchaseSuccessScreen(navController = navController)
            }
            composable<NavigationRoute.PurchaseFailed> {
                PurchaseFailedScreen(navController = navController)
            }
            composable<NavigationRoute.Feedback> {
                FeedbackScreen(navController = navController)
            }
            composable<NavigationRoute.FeedbackSuccess> {
                FeedbackSuccessScreen(navController = navController)
            }
        }
    }

    private fun startCheckout(purchaseData: PurchaseData) {
        lifecycleScope.launch {
            val isSuccess = billingService.get()
                .launchBillingFlow(
                    activity = this@MainActivity,
                    purchaseData = purchaseData
                )
            if (!isSuccess) {
                showToast(
                    message = getString(R.string.subscription_checkout_failed)
                )
            }
        }
    }
}