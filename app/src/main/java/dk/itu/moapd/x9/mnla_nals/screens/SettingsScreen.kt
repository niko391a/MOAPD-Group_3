package dk.itu.moapd.x9.mnla_nals.screens

import android.app.LocaleManager
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
import android.os.Build
import android.os.LocaleList
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dk.itu.moapd.x9.mnla_nals.R
import dk.itu.moapd.x9.mnla_nals.ViewModels.AuthViewModel
import dk.itu.moapd.x9.mnla_nals.ViewModels.SettingsViewModel
import dk.itu.moapd.x9.mnla_nals.ui.theme.AppTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel(),
) {
    Column(modifier = modifier.fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        AccountInfo(authViewModel)

        Spacer(modifier = Modifier.height(32.dp))

        SettingsThemeToggle(settingsViewModel)

        if (Build.VERSION.SDK_INT >= 33) {
            Spacer(modifier = Modifier.height(32.dp))
            // This is the same as Build.VERSION_CODES.TIRAMISU
            SettingsLanguageSelector()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsThemeToggle(
    settingsViewModel: SettingsViewModel
) {
    val currentTheme by settingsViewModel.currentTheme.collectAsStateWithLifecycle()
    var expanded by rememberSaveable  { mutableStateOf(false) }
    val themeTypes = stringArrayResource(R.array.Themes)

    // Map display strings to enum values for the ViewModel
    val themeMap = mapOf(
        themeTypes[0] to AppTheme.STANDARD,
        themeTypes[1] to AppTheme.LIGHT,
        themeTypes[2] to AppTheme.DARK,
        themeTypes[3] to AppTheme.RAINBOW,
        themeTypes[4] to AppTheme.ULTRA_DARK,
    )

    // Reverse lookup: enum -> display string
    val displayName = themeMap.entries.find { it.value == currentTheme }?.key ?: themeTypes[0]

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = displayName,
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
                        expanded = false
                        settingsViewModel.setTheme(themeMap[type] ?: AppTheme.STANDARD)
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
        context.getSystemService(LocaleManager::class.java)
            .applicationLocales = LocaleList(Locale.forLanguageTag("en"))
    }) {
        Text(stringResource(R.string.settings_language_en_text))
    }

    Button(onClick = {
        context.getSystemService(LocaleManager::class.java)
            .applicationLocales = LocaleList(Locale.forLanguageTag("da"))
    }) {
        Text(stringResource(R.string.settings_language_da_text))
    }
}

@Composable
fun AccountInfo(authViewModel: AuthViewModel) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Text(
            text = "Filled",
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = { authViewModel.signOut() }
        ) {
            Text(stringResource(R.string.settings_sign_out))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}