package dk.itu.moapd.x9.mnla_nals.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dk.itu.moapd.x9.mnla_nals.R

@Composable
fun BasicDropdownMenu(
    reportType: String,         // The current value
    reportTypes: Array<String>,  // The list of options
    onTypeSelected: (String) -> Unit // The callback to update the state
) {
    var expanded by remember { mutableStateOf(false) }

    // Wrap in a Box to provide a coordinate parent for the menu
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = reportType,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(id = R.string.create_report_type)) },
            trailingIcon = {
                // Using a standard IconButton for the toggle
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // This invisible box sits on top of the TextField to catch clicks
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { expanded = !expanded }
        )

        // The standard DropdownMenu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            // This ensures the menu aligns with the start of the Box
            modifier = Modifier.fillMaxWidth(0.9f) // Optional: Adjust width manually
        ) {
            reportTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        // 1. Tell the parent the value changed
                        onTypeSelected(type)
                        // 2. Close the menu
                        expanded = false
                    }
                )
            }
        }
    }
}
