package dk.itu.moapd.x9.mnla_nals.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SeverityPill(severity: String) {
    // Map severity string to background color.
    val backgroundColor = when (severity.lowercase()) {
        "low"      -> Color(0xFFD4EDDA) // soft green
        "medium"   -> Color(0xFFFFF3CD) // soft yellow — matches your screenshot
        "high"     -> Color(0xFFF8D7DA) // soft red
        else       -> Color(0xFFE2E3E5) // neutral grey for unknown values
    }

    // Text color should contrast with the background
    val textColor = when (severity.lowercase()) {
        "critical" -> Color.White
        else       -> Color(0xFF333333)
    }

    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(50) // 50% = full pill shape
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = severity,
            color = textColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}