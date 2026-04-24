package dk.itu.moapd.x9.mnla_nals.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import dk.itu.moapd.x9.mnla_nals.R
import dk.itu.moapd.x9.mnla_nals.ViewModels.PermissionViewModel
import dk.itu.moapd.x9.mnla_nals.components.PermissionGranter

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    navigate: () -> Unit,
    snackbarHostState: SnackbarHostState,
    permissionViewModel: PermissionViewModel = viewModel(),

    ) {

    PermissionGranter(permissionViewModel)

    // Itu's coordinates
    val itu = LatLng(55.6596, 12.5910)
    // need to show all user reports with markers
    /*
    here Itu cords are hard code can use this and might need the report id asweel as something to show it
    Maybe geofencing
     */

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(itu, 15f)
    }


    if (permissionViewModel.requestPermission.collectAsState().value) {
        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = true,
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
        PermissionRequiredScreen(
            modifier = modifier,
            onGoBack = navigate
        )
    }
}