package dk.itu.moapd.x9.mnla_nals

import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import java.util.Locale
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
fun SettingsScreen(modifier: Modifier = Modifier, onThemeChanged: (String) -> Unit, currentTheme: String) {
    Column(modifier = modifier.fillMaxSize(),
    ) {
        if (Build.VERSION.SDK_INT >= 33) {
            // This is the same as Build.VERSION_CODES.TIRAMISU
            SettingsLanguageSelector()
        }
        SettingsThemeToggle(onThemeChanged = onThemeChanged, currentTheme = currentTheme)
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsThemeToggle(onThemeChanged: (String) -> Unit, currentTheme: String) {
    var themeType by rememberSaveable  { mutableStateOf(currentTheme) }
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

// Min API 33+ friendly version:
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SettingsLanguageSelector() {
    val context = LocalContext.current

    Button(onClick = {
        context.getSystemService(android.app.LocaleManager::class.java)
            .applicationLocales = LocaleList(Locale.forLanguageTag("en"))
    }) {
        Text(stringResource(R.string.settings_language_en_text))
    }

    Button(onClick = {
        context.getSystemService(android.app.LocaleManager::class.java)
            .applicationLocales = LocaleList(Locale.forLanguageTag("da"))
    }) {
        Text(stringResource(R.string.settings_language_da_text))
    }
}
