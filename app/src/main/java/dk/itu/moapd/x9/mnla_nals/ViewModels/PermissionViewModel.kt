package dk.itu.moapd.x9.mnla_nals.ViewModels


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PermissionViewModel : ViewModel() {
    private val _requestPermission = MutableStateFlow(false)
    val requestPermission: StateFlow<Boolean> = _requestPermission

    fun onPermissionButtonClicked() {
        _requestPermission.value = true
    }

    fun onPermissionRequestHandled() {
        _requestPermission.value = false
    }
}