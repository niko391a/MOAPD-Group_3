package dk.itu.moapd.x9.mnla_nals

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringArrayResource


@Composable
fun SettingsScreen(modifier: Modifier = Modifier, onThemeChanged: (String) -> Unit) {
    Column(modifier = modifier.fillMaxSize(),
    ) {
        SettingsThemeToggle(onThemeChanged = onThemeChanged)
        SettingsLanguageSelector()
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsThemeToggle(onThemeChanged: (String) -> Unit) {
    var themeType by rememberSaveable  { mutableStateOf("Standard") }
    var expanded by rememberSaveable  { mutableStateOf(false) }
    val themeTypes = stringArrayResource(R.array.Themes)
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = themeType,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(id = R.string.theme_settings)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            themeTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        themeType = type
                        expanded = false
                        onThemeChanged(type)
                    }
                )
            }
        }
    }
}

@Composable
fun SettingsLanguageSelector() {
    // Placeholder for language selector
    Text(text = "Language Selector goes here")
}

