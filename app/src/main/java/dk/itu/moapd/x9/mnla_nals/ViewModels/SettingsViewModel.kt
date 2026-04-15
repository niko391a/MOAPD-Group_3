package dk.itu.moapd.x9.mnla_nals.ViewModels

import androidx.lifecycle.ViewModel
import dk.itu.moapd.x9.mnla_nals.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel: ViewModel()  {
    private val _currentTheme = MutableStateFlow(AppTheme.STANDARD)
    val currentTheme: StateFlow<AppTheme> = _currentTheme.asStateFlow()

    // Track which language tag is active ("en" or "da")
    private val _currentLocaleTag = MutableStateFlow("en")
    val currentLocaleTag: StateFlow<String> = _currentLocaleTag.asStateFlow()

    fun setTheme(theme: AppTheme) {
        _currentTheme.value = theme
    }

    fun setLocale(tag: String) {
        _currentLocaleTag.value = tag
    }
}