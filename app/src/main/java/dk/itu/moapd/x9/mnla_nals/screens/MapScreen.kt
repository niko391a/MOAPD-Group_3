package dk.itu.moapd.x9.mnla_nals.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

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
import dk.itu.moapd.x9.mnla_nals.data.Report

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
    val reports by reportViewModel.reports.collectAsState()
    var selectedReport by remember { mutableStateOf<Report?>(null) }
    var currentSpeed by remember { mutableFloatStateOf(0f) }
    
    // need to show all user reports with markers
    /*
    here Itu cords are hard code can use this and might need the report id asweel as something to show it
    Maybe geofencing
     */
    // update to persons position and show it on the map
    @SuppressLint("MissingPermission")
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val userLocation = LatLng(location.latitude, location.longitude)
                        cameraPositionState.move(
                            com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(userLocation, 15f)
                        )
                        // Convert speed from m/s to km/h (multiply by 3.6)
                        currentSpeed = location.speed * 3.6f
                    }
                }
        }
    }

    if (hasPermission) {
        if (selectedReport != null) {
            Log.d("MapScreen", "Selected report: $selectedReport")
            ReportDetailScreen(
                selectedReport = selectedReport!!,
                navigate = { selectedReport = null },
                modifier = modifier,
            )
        }else {
            Box(
                modifier = modifier.fillMaxSize()
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
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
                            snippet = report.description,
                            onClick = {
                                selectedReport = report
                                true
                            }

                        )
                        Log.d("MapScreen", "Report: $report")
                    }
                }
                // Speed display at bottom right
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.7f),
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(12.dp),
                ) {
                    Text(
                        text = String.format("%.1f km/h", currentSpeed),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }

    }else{
            PermissionRequiredScreen(
                modifier = modifier,
                onGoBack = navigate
        )
    }
}