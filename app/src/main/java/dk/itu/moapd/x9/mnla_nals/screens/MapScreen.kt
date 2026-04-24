package dk.itu.moapd.x9.mnla_nals.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import dk.itu.moapd.x9.mnla_nals.R
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import dk.itu.moapd.x9.mnla_nals.ViewModels.PermissionViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    navigate: () -> Unit,
    snackbarHostState: SnackbarHostState,
    permissionViewModel: PermissionViewModel = viewModel(),

    ) {

    val multiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    // Itu's coordinates
    val itu = LatLng(55.6596, 12.5910)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(itu, 15f)
    }
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

    if (permissionViewModel.requestPermission.collectAsState().value) {
        GoogleMap(
            modifier = modifier.fillMaxSize(), // 3. Use modifier, not Modifier
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = true, // 4. Just use true here
            ),
        ) {
            // creates the read markers on the map
            val markerState = remember {
                MarkerState(position = itu)
            }
            Marker(
                state = markerState,
                title = stringResource(R.string.itu_title),
            )
        }
    } else {
        Text("Request Location Permissions")

    }
}