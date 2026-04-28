package dk.itu.moapd.x9.mnla_nals.components

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dk.itu.moapd.x9.mnla_nals.ViewModels.PermissionViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionGranter(
    permissionViewModel: PermissionViewModel,
) {
    val permissions = buildList {
        add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(android.Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

    // Launch the dialog once on first composition
    LaunchedEffect(Unit) {
        if (!multiplePermissionsState.allPermissionsGranted) {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }
    }

    // React to permission state changes (after dialog is answered)
    LaunchedEffect(multiplePermissionsState.allPermissionsGranted) {
        if (multiplePermissionsState.allPermissionsGranted) {
            permissionViewModel.onPermissionButtonClicked()
        } else {
            permissionViewModel.onPermissionRequestHandled()
        }
    }
}
