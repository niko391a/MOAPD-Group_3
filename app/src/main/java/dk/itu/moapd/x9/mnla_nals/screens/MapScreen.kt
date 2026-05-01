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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt
import java.util.Locale

import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import dk.itu.moapd.x9.mnla_nals.ViewModels.AuthViewModel
import dk.itu.moapd.x9.mnla_nals.ViewModels.PermissionViewModel
import dk.itu.moapd.x9.mnla_nals.ViewModels.ReportViewModel
import dk.itu.moapd.x9.mnla_nals.components.PermissionGranter
import dk.itu.moapd.x9.mnla_nals.data.Report
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    navigate: () -> Unit,
    snackbarHostState: SnackbarHostState,
    permissionViewModel: PermissionViewModel = viewModel(),
    reportViewModel: ReportViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    onAddReport: () -> Unit = {},

    ) {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    PermissionGranter(permissionViewModel)
    val hasPermission = permissionViewModel.requestPermission.collectAsState().value
    val user by authViewModel.user.collectAsStateWithLifecycle()
    val latestUserUid = rememberUpdatedState(user?.uid.orEmpty())
    val scope = rememberCoroutineScope()
    
    // Defaults to itu
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(55.6596, 12.5910), 15f)
    }
    val reports by reportViewModel.reports.collectAsState()
    var selectedReport by remember { mutableStateOf<Report?>(null) }
    var currentSpeed by remember { mutableFloatStateOf(0f) }

    // To create a report incase of a crash we first need to monitor the sensor
    val sensorManager = remember {
        context.getSystemService(android.content.Context.SENSOR_SERVICE) as SensorManager
    }
    // Time of the impact
    var lastImpactTimeMs by remember { mutableLongStateOf(0L) }
    var lastReportTimeMs by remember { mutableLongStateOf(0L) }

    // latest location
    var lastLat by remember { mutableStateOf<Double?>(null) }
    var lastLng by remember { mutableStateOf<Double?>(null) }


    var speedAtImpact by remember { mutableFloatStateOf(0f) }
    
    // need to show all user reports with markers
    /*
    here Itu cords are hard code can use this and might need the report id asweel as something to show it
    Maybe geofencing
     */
    // update to persons position and show it on the map
    var hasMovedCameraOnce by remember { mutableStateOf(false) }

    DisposableEffect(hasPermission) {
        if (!hasPermission) {
            return@DisposableEffect onDispose {}
        }

        // update speed every second
        val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 2000L // 2000ms = 2 seconds
        ).setMinUpdateIntervalMillis(1000L).build()
        val linearAccelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        val accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val sensor = linearAccelSensor ?: accelSensor
        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val magnitude = sqrt(x * x + y * y + z * z)
                Log.d("MapScreen", "Sensor changed: magnitude=$magnitude")


                // --- Crash candidate detection (impact) ---
                // Use different thresholds depending on sensor type.
                val impactThreshold =
                    if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) 8f else 15f
                val minSpeedForImpactKmh = 5f
                val impactDebounceMs = 1_000L

                val now = System.currentTimeMillis()
                val canRegisterNewImpact = (now - lastImpactTimeMs) > impactDebounceMs

                // Only consider an impact if the user was moving (avoids phone drops while standing still).
                if (canRegisterNewImpact && currentSpeed >= minSpeedForImpactKmh && magnitude >= impactThreshold) {
                    lastImpactTimeMs = now
                    speedAtImpact = currentSpeed
                    Log.d(
                        "MapScreen",
                        "Potential crash detected! sensor=${event.sensor.type} magnitude=$magnitude speedAtImpact=$speedAtImpact km/h"
                    )
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val didRegisterSensorListener = if (sensor != null) {
            sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Log.w("MapScreen", "No accelerometer/linear-acceleration sensor available; crash detection disabled")
            false
        }

        // location callback

        val locationCallback = object : com.google.android.gms.location.LocationCallback() {
            override fun onLocationResult(result: com.google.android.gms.location.LocationResult) {
                result.lastLocation?.let { location ->
                    // update location
                    lastLat = location.latitude
                    lastLng = location.longitude
                    // Update speed (convert m/s to km/h, default to 0 if unavailable)
                    currentSpeed = if (location.hasSpeed()) location.speed * 3.6f else 0f // 3.6 floating point to convert speed to km /h
                    val now = System.currentTimeMillis()

                    // creates the report after 5 seconds
                    val confirmationWindowMs = 5_000L // 5000 ms is 5 seconds
                    val cooldownMs = 50_000L // cooldown between reports to make it not instantly trigger again 50 seconds


                    val hadRecentImpact = (lastImpactTimeMs != 0L) && (now - lastImpactTimeMs <= confirmationWindowMs)
                    val cooldownOver = (now - lastReportTimeMs >= cooldownMs)


                    if (hadRecentImpact && cooldownOver && currentSpeed <= 1.0f && speedAtImpact >= 5.0f) {
                        val lat = lastLat
                        val lng = lastLng
                        if (lat != null && lng != null) {
                            Log.d("MapScreen", "Creating crash report")
                            val report = Report(
                                uid = latestUserUid.value,
                                title = "Possible crash detected",
                                description = "Auto-generated report (impact + abrupt stop). Please verify.",
                                type = "Accident",
                                severity = "High",
                                latitude = lat,
                                longitude = lng,
                                language = "en"
                            )
                            reportViewModel.addReport(report)
                            lastReportTimeMs = now

                            scope.launch {
                                snackbarHostState.showSnackbar("Auto crash report created")
                            }
                        }

                        // reset impact so it doesn't re-trigger repeatedly in the window
                        lastImpactTimeMs = 0L
                    }
                    
                    

                    if (!hasMovedCameraOnce) {
                        val userLocation = LatLng(location.latitude, location.longitude)
                        cameraPositionState.move(
                            com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(userLocation, 15f)
                        )
                        hasMovedCameraOnce = true
                    }
                }
            }
        }

        //request location updates
        @SuppressLint("MissingPermission")
        fun startUpdates() {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                android.os.Looper.getMainLooper()
            )
        }
        startUpdates()

        // dispose to save battery
        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            if (didRegisterSensorListener) {
                sensorManager.unregisterListener(sensorListener)
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
                onAddReport = onAddReport,
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
                        text = String.format(Locale.getDefault(), "%.1f km/h", currentSpeed),
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