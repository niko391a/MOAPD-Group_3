package dk.itu.moapd.x9.mnla_nals.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dk.itu.moapd.x9.mnla_nals.ViewModels.PermissionViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionGranter(
    permissionViewModel: PermissionViewModel,
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    if (!permissionViewModel.requestPermission.collectAsState().value) {
        LaunchedEffect(Unit) {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }
        if (multiplePermissionsState.allPermissionsGranted) {
            permissionViewModel.onPermissionButtonClicked()
        } else {
            permissionViewModel.onPermissionRequestHandled()
        }
    }
}