package dk.itu.moapd.x9.mnla_nals.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Some sample button i found online.
@Composable
fun AnimatedColorToggleButton(
    buttonText : String,
    modifier : Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // 1. Grab the colors from the current theme (which might be dynamic)
    val colorScheme = MaterialTheme.colorScheme

    // 2. Map state to theme roles
    // Selected = primary color, Unselected = surface/variant color
    val targetColor = if (isSelected) colorScheme.primary else colorScheme.surfaceVariant
    val contentColor = if (isSelected) colorScheme.onPrimary else colorScheme.onSurfaceVariant

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        label = "DynamicButtonAnimation"
    )

    Button(
        onClick = { onClick() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedColor,
            contentColor = contentColor
        ),
    ) {
        Text(
            text = buttonText,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            autoSize = TextAutoSize.StepBased(minFontSize = 1.sp, maxFontSize = 16.sp),
        )
    }
}