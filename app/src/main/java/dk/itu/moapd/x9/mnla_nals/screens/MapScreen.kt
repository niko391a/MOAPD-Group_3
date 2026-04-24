package dk.itu.moapd.x9.mnla_nals.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import dk.itu.moapd.x9.mnla_nals.ViewModels.PermissionViewModel
import dk.itu.moapd.x9.mnla_nals.ViewModels.ReportViewModel
import dk.itu.moapd.x9.mnla_nals.components.PermissionGranter

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    navigate: () -> Unit,
    snackbarHostState: SnackbarHostState,
    permissionViewModel: PermissionViewModel = viewModel(),
    reportViewModel: ReportViewModel = viewModel()

    ) {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    PermissionGranter(permissionViewModel)
    val hasPermission = permissionViewModel.requestPermission.collectAsState().value

    // Defaults to itu
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(55.6596, 12.5910), 15f)
    }
    // need to show all user reports with markers
    /*
    here Itu cords are hard code can use this and might need the report id asweel as something to show it
    Maybe geofencing
     */
    // update to persons position and show it on the map
    @SuppressLint("MissingPermission")
    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { location ->
            if (location != null) {
                val userLocation = LatLng(location.latitude, location.longitude)
                // Animate camera to the new position
                cameraPositionState.move(
                    com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(userLocation, 15f)
                )
            }
        }

    val reports by reportViewModel.reports.collectAsState()

    if (hasPermission) {
        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = true,
            ),
        ) {
            reports.forEach { report ->
                val reportPosition = LatLng(report.latitude, report.longitude)
                    Marker(
                        state = MarkerState(position = reportPosition),
                        title = report.title,
                        snippet = report.description
                    )
                Log.d("MapScreen", "Report: $report")
            }
        }
    }else{
            PermissionRequiredScreen(
                modifier = modifier,
                onGoBack = navigate
        )
    }
}