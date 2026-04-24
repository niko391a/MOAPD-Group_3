package dk.itu.moapd.x9.mnla_nals.ViewModels

import android.app.Application
import android.app.LocaleManager
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import dk.itu.moapd.x9.mnla_nals.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val _currentTheme = MutableStateFlow(AppTheme.STANDARD)
    val currentTheme: StateFlow<AppTheme> = _currentTheme.asStateFlow()

    private val _currentLocaleTag = MutableStateFlow(readPersistedLocale())
    val currentLocaleTag: StateFlow<String> = _currentLocaleTag.asStateFlow()

    private fun readPersistedLocale(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return getApplication<Application>()
                .getSystemService(LocaleManager::class.java)
                .applicationLocales.get(0)?.language ?: "en"
        }
        return "en"
    }

    fun setTheme(theme: AppTheme) {
        _currentTheme.value = theme
    }

    fun setLocale(tag: String) {
        _currentLocaleTag.value = tag
    }
}