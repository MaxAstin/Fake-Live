package com.bunbeauty.tiptoplive.common.ui.components.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.util.rememberMultipleEventsCutter

@Composable
fun FakeLivePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = FakeLiveTheme.colors.interactive,
        contentColor = FakeLiveTheme.colors.onSurface,
        disabledContainerColor = Color(0xFFE7F1FE),
        disabledContentColor = Color(0xFF9CB9DC)
    ),
    contentPadding: PaddingValues = PaddingValues(),
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
) {
    val multipleEventsCutter = rememberMultipleEventsCutter()

    Button(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        colors = colors,
        enabled = enabled,
        onClick = {
            multipleEventsCutter.processEvent(onClick)
        },
        contentPadding = contentPadding
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            leadingIcon()
            Text(
                text = text,
                style = FakeLiveTheme.typography.titleSmall,
            )
            trailingIcon()
        }
    }
}

@Preview
@Composable
private fun FakeLivePrimaryButtonPreview() {
    FakeLiveTheme {
        FakeLivePrimaryButton(
            text = "Button",
            onClick = {}
        )
    }
}