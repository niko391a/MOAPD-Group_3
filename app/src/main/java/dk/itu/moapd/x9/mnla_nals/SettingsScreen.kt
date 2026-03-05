package dk.itu.moapd.x9.mnla_nals

import android.os.LocaleList
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
        SettingsLanguageSelector()
    }

}

@Composable
fun SettingsThemeToggle() {
    // Placeholder for theme toggle button
    Text(text = "Theme Toggle Button goes here")
}

// Min API 28+ friendly version:
//@Composable
//fun SettingsLanguageSelector() {
//    val context = LocalContext.current
//
//    // Example button to switch to Danish
//    Button(onClick = {
//        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("da")
//        AppCompatDelegate.setApplicationLocales(appLocale)
//    }) {
//        Text("Switch to Danish")
//    }
//
//    // Example button to switch to English
//    Button(onClick = {
//        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("en")
//        AppCompatDelegate.setApplicationLocales(appLocale)
//    }) {
//        Text("Switch to English")
//    }
//}

// Min API 33+ friendly version:
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
