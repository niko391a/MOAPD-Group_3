package dk.itu.moapd.x9.mnla_nals.screens

import android.app.LocaleManager
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseUser
import dk.itu.moapd.x9.mnla_nals.R
import dk.itu.moapd.x9.mnla_nals.ViewModels.AuthViewModel
import dk.itu.moapd.x9.mnla_nals.ViewModels.SettingsViewModel
import dk.itu.moapd.x9.mnla_nals.components.AnimatedColorToggleButton
import dk.itu.moapd.x9.mnla_nals.components.BasicDropdownMenu
import dk.itu.moapd.x9.mnla_nals.ui.theme.AppTheme
import java.util.Locale

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel(),
) {
    // Collect here at the top level so child composables receive plain values,
    // not StateFlows — this is the idiomatic "state hoisting" pattern in Compose
    val user by authViewModel.user.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        SettingsSection(
            icon = Icons.Default.AccountCircle,
            title = stringResource(R.string.settings_label_account)
        ) {
            AccountInfo(
                user = user,
                onSignOut = { authViewModel.signOut() }
            )
        }

        SettingsSection(
            icon = Icons.Default.Settings,
            title = stringResource(R.string.settings_label_theme)
        ) {
            SettingsThemeToggle(settingsViewModel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            SettingsSection(icon = Icons.Default.Settings, title = stringResource(R.string.settings_label_language)) {
                SettingsLanguageSelector(
                    onLocaleSelected = { tag -> settingsViewModel.setLocale(tag) },
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}

/**
 * Reusable section wrapper. Uses a slot API (content lambda) so each section
 * controls its own internals while sharing the same header chrome.
 */
@Composable
fun SettingsSection(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        content()
    }
}

@Composable
fun SettingsThemeToggle(settingsViewModel: SettingsViewModel) {
    val currentTheme by settingsViewModel.currentTheme.collectAsStateWithLifecycle()
    val themeTypes = stringArrayResource(R.array.Themes)

    val themeMap = mapOf(
        themeTypes[0] to AppTheme.STANDARD,
        themeTypes[1] to AppTheme.LIGHT,
        themeTypes[2] to AppTheme.DARK,
        themeTypes[3] to AppTheme.RAINBOW,
    )

    val displayName = themeMap.entries.find { it.value == currentTheme }?.key ?: themeTypes[0]

    BasicDropdownMenu(
        selectedValue = displayName,
        dropdownOptions = themeTypes,
        onTypeSelected = { selected ->
            settingsViewModel.setTheme(themeMap[selected] ?: AppTheme.STANDARD)
        }
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SettingsLanguageSelector(
    onLocaleSelected: (String) -> Unit,
    settingsViewModel: SettingsViewModel
) {
    val context = LocalContext.current
    val currentLocaleTag by settingsViewModel.currentLocaleTag.collectAsStateWithLifecycle()

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AnimatedColorToggleButton(
            buttonText = stringResource(R.string.settings_language_en_text),
            isSelected = currentLocaleTag == "en",
            modifier = Modifier.weight(1f),
            onClick = {
                onLocaleSelected("en")
                context.getSystemService(LocaleManager::class.java)
                    .applicationLocales = LocaleList(Locale.forLanguageTag("en"))
                settingsViewModel.setLocale("en")
            }
        )

        AnimatedColorToggleButton(
            buttonText = stringResource(R.string.settings_language_da_text),
            isSelected = currentLocaleTag == "da",
            modifier = Modifier.weight(1f),
            onClick = {
                onLocaleSelected("da")
                context.getSystemService(LocaleManager::class.java)
                    .applicationLocales = LocaleList(Locale.forLanguageTag("da"))
                settingsViewModel.setLocale("da")
            }
        )
    }
}

/**
 * Receives user state and a sign-out callback instead of the ViewModel directly.
 * This makes AccountInfo easier to preview and test in isolation — it has no
 * knowledge of where the data comes from, only how to display it.
 */
@Composable
fun AccountInfo(
    user: FirebaseUser?,
    onSignOut: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    // Anonymous users have null email — handle both cases
                    text = user?.email ?: stringResource(R.string.settings_label_guest),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedButton(onClick = onSignOut) {
                Text(stringResource(R.string.settings_sign_out))
            }
        }
    }
}