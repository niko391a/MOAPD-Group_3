package dk.itu.moapd.x9.mnla_nals.ViewModels

import androidx.lifecycle.ViewModel
import dk.itu.moapd.x9.mnla_nals.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel: ViewModel()  {
    // Expose screen UI state
    private val _currentTheme = MutableStateFlow(AppTheme.STANDARD)
    val currentTheme: StateFlow<AppTheme> = _currentTheme.asStateFlow()

    fun setTheme(theme: AppTheme) {
        _currentTheme.value = theme
    }
}