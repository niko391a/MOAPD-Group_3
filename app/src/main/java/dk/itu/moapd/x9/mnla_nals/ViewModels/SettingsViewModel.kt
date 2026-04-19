package dk.itu.moapd.x9.mnla_nals.ViewModels

import android.app.Application
import android.app.LocaleManager
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import dk.itu.moapd.x9.mnla_nals.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class SettingsViewModel(application: Application) : AndroidViewModel(application)  {
    private val _currentTheme = MutableStateFlow(AppTheme.STANDARD)
    val currentTheme: StateFlow<AppTheme> = _currentTheme.asStateFlow()

    // Track which language tag is active ("en" or "da")
    private val _currentLocaleTag = MutableStateFlow(getInitialLocaleTag())
    val currentLocaleTag: StateFlow<String> = _currentLocaleTag.asStateFlow()

    private fun getInitialLocaleTag(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = getApplication<Application>()
                .getSystemService(LocaleManager::class.java)

            // applicationLocales is empty if the user hasn't explicitly set one,
            // so we fall back to the device default
            val tag = localeManager.applicationLocales[0]?.language
            tag ?: Locale.getDefault().language
        } else {
            Locale.getDefault().language // fallback for older Android versions
        }
    }

    fun setTheme(theme: AppTheme) {
        _currentTheme.value = theme
    }

    fun setLocale(tag: String) {
        _currentLocaleTag.value = tag
    }
}