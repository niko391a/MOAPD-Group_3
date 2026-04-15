package dk.itu.moapd.x9.mnla_nals.ViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SnackViewModel : ViewModel() {

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    fun sendSnackbarMessage(message: String) {
        viewModelScope.launch {
            _snackbarMessage.emit(message)
        }
    }
}