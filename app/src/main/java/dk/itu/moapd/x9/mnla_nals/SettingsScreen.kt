package dk.itu.moapd.x9.mnla_nals

import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize(),
    ) {
        SettingsThemeToggle()
        // Only show the selector if the device is API 33 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            SettingsLanguageSelector()
        } else {
            // Optional: Show a fallback or nothing at allAAS
            Text("Language settings are managed in System Settings on this version.")
        }    }

}

@Composable
fun SettingsThemeToggle() {
    // Placeholder for theme toggle button
    Text(text = "Theme Toggle Button goes here")
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
        Text("Switch to English")
    }

    Button(onClick = {
        context.getSystemService(android.app.LocaleManager::class.java)
            .applicationLocales = LocaleList(Locale.forLanguageTag("da"))
    }) {
        Text("Switch to Danish")
    }
}
