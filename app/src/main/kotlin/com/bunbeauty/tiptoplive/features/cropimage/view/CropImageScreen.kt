package com.bunbeauty.tiptoplive.features.cropimage.view

import android.net.Uri
import android.widget.LinearLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.navigation.NavigationRoute
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLiveDialogButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.features.cropimage.presentation.CropImage
import com.bunbeauty.tiptoplive.features.cropimage.presentation.CropImageViewModel
import com.canhub.cropper.CropImageView
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun CropImageScreen(
    navController: NavController,
    uri: Uri?,
    onMockClick: () -> Unit
) {
    val viewModel: CropImageViewModel = hiltViewModel()
    val onAction = remember {
        { action: CropImage.Action ->
            viewModel.onAction(action)
        }
    }
    val cropEvent = remember {
        Channel<Unit>()
    }

    LaunchedEffect(Unit) {
        viewModel.event.onEach { event ->
            when (event) {
                CropImage.Event.NavigateBack -> {
                    navController.navigate(route = NavigationRoute.Preparation) {
                        popUpTo<NavigationRoute.Preparation> {
                            inclusive = true
                        }
                    }
                }
            }
        }.launchIn(this)
    }

    Column(
        modifier = Modifier
            .background(FakeLiveTheme.colors.background)
            .fillMaxSize()
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .clickableWithoutIndication {
                        navController.popBackStack()
                    },
                imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                contentDescription = "Close",
                tint = FakeLiveTheme.colors.onBackground,
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .clickableWithoutIndication {
                        onMockClick()
                    },
                imageVector = ImageVector.vectorResource(R.drawable.ic_magic_wand),
                contentDescription = "Magic wand",
                tint = FakeLiveTheme.colors.onBackground,
            )
        }
        CropImageView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            uri = uri,
            cropEvent = cropEvent.consumeAsFlow(),
            onAction = onAction
        )

        val scope = rememberCoroutineScope()
        FakeLiveDialogButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(12.dp),
            text = stringResource(R.string.crop_image_next),
            iconId = R.drawable.ic_forward,
            shape = RoundedCornerShape(40.dp),
            background = FakeLiveTheme.colors.interactive,
            onClick = {
                scope.launch {
                    cropEvent.send(Unit)
                }
            }
        )
    }
}

@Composable
private fun CropImageView(
    uri: Uri?,
    cropEvent: Flow<Unit>,
    onAction: (CropImage.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    uri ?: return

    val scope = rememberCoroutineScope()
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.align(Alignment.Center),
            factory = { context ->
                val cropImageView = CropImageView(context)

                scope.launch {
                    cropEvent.first()
                    cropImageView.croppedImageAsync()
                }

                LinearLayout(context).apply {
                    cropImageView.apply {
                        setImageCropOptions(CropImageDefaults.options())
                        setImageUriAsync(uri)
                    }.also { view ->
                        val listener = CropImageView.OnCropImageCompleteListener { _, result ->
                            onAction(
                                CropImage.Action.CropClick(
                                    uri = result.uriContent.toString()
                                )
                            )
                        }
                        view.setOnCropImageCompleteListener(listener)
                    }
                    addView(cropImageView)
                }
            }
        )
    }
}

@Preview
@Composable
private fun CropImageScreenPreview() {
    FakeLiveTheme {
        CropImageScreen(
            navController = rememberNavController(),
            uri = Uri.EMPTY,
            onMockClick = {}
        )
    }
}