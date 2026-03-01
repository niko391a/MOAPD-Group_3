package dk.itu.moapd.x9.mnla_nals
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dk.itu.moapd.x9.mnla_nals.ui.theme.X9Theme
import android.util.Log
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import dk.itu.moapd.x9.mnla_nals.data.Report

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("Info","On Create was called for MainActivity")
        setContent {
            var selectedTheme by rememberSaveable { mutableStateOf("Standard") }

            appTheme(theme = selectedTheme) {
                AppNavigationBar(onThemeChanged = { theme ->
                    selectedTheme = theme
                })
            }
        }
    }
}

@Composable
fun AppNavigationBar(onThemeChanged: (String) -> Unit) {
    var selectedNavItem by rememberSaveable  { mutableIntStateOf(0) }
    val reports = rememberSaveable { mutableStateListOf<Report>() }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedNavItem == 0,
                    onClick = { selectedNavItem = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text(stringResource(id = R.string.nav_home)) }
                )
                NavigationBarItem(
                    selected = selectedNavItem == 1,
                    onClick = { selectedNavItem = 1 },
                    icon = { Icon(Icons.Default.List, contentDescription = "Reports") },
                    label = { Text(stringResource(id = R.string.nav_report)) }
                )
                NavigationBarItem(
                    selected = selectedNavItem == 2,
                    onClick = { selectedNavItem = 2 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text(stringResource(id = R.string.nav_settings)) }
                )
            }
        }
    ) { innerPadding ->
        when (selectedNavItem) {
            0 -> {
                HomeScreen(
                    reports = reports,
                    modifier = Modifier.padding(innerPadding),
                    onAddReport = { selectedNavItem = 1 }
                )
            }
            1 -> {
                CreateReportScreen(Modifier.padding(innerPadding),
                    onSubmitReport = { report ->
                        reports.add(report)
                        selectedNavItem = 0
                        scope.launch {
                            snackbarHostState.showSnackbar("Traffic report successfully created!")
                        }
                    })
            }
            2 -> {
                SettingsScreen(Modifier.padding(innerPadding), onThemeChanged = onThemeChanged)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    X9Theme {
        AppNavigationBar(onThemeChanged = {})
    }
}

@Composable
fun appTheme(theme: String, content: @Composable () -> Unit) {
    when (theme) {
        "Standard" -> X9Theme(darkTheme = false, content = content)
        "Light" -> X9Theme(darkTheme = false, content = content)
        "Dark" -> X9Theme(darkTheme = true, content = content)
        "Rainbow" -> X9Theme(darkTheme = false, content = content) // Add custom rainbow theme later
        "Ultra Dark" -> X9Theme(darkTheme = true, content = content) // Add custom ultra dark theme later
        else -> X9Theme(darkTheme = false, content = content)
    }
}
