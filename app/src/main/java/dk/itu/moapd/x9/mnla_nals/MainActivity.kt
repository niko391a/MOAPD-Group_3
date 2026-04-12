package dk.itu.moapd.x9.mnla_nals
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.FirebaseApp
import dk.itu.moapd.x9.mnla_nals.ViewModels.AuthViewModel
import dk.itu.moapd.x9.mnla_nals.ViewModels.ReportViewModel
import dk.itu.moapd.x9.mnla_nals.ViewModels.SettingsViewModel
import dk.itu.moapd.x9.mnla_nals.ViewModels.SnackViewModel
import dk.itu.moapd.x9.mnla_nals.screens.CreateReportScreen
import dk.itu.moapd.x9.mnla_nals.screens.HomeScreen
import dk.itu.moapd.x9.mnla_nals.screens.LoginScreen
import dk.itu.moapd.x9.mnla_nals.screens.SettingsScreen
import dk.itu.moapd.x9.mnla_nals.ui.theme.AppTheme
import dk.itu.moapd.x9.mnla_nals.ui.theme.CustomThemes
import dk.itu.moapd.x9.mnla_nals.ui.theme.X9Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()

        Log.d("Info","On Create was called for MainActivity")

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val authViewModel: AuthViewModel = viewModel()
            val reportViewModel: ReportViewModel = viewModel()

            val user by authViewModel.user.collectAsStateWithLifecycle()
            val selectedTheme by settingsViewModel.currentTheme.collectAsStateWithLifecycle()

            AppTheme(theme = selectedTheme) {
                if(user == null) { // logging is a guest result is null.
                    LoginScreen()
                } else {
                    AppNavigationBar()
                }
            }
        }
    }
}

@Composable
fun AppNavigationBar(
    authViewModel: AuthViewModel = viewModel(),
    snackViewModel: SnackViewModel = viewModel(),
) {

    var selectedNavItem by rememberSaveable  { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val user by authViewModel.user.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        snackViewModel.snackbarMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (user?.isAnonymous == true) {
                GuestBanner()
            }
        },
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
                    modifier = Modifier.padding(innerPadding),
                )
            }
            1 -> {
                if (user?.isAnonymous == true) {
                    LaunchedEffect(Unit) {
                        snackbarHostState.showSnackbar("You need to sign in to access this feature")
                    }
                }else {
                    CreateReportScreen(
                        Modifier.padding(innerPadding),
                        navigate = { selectedNavItem = 0 },
                        snackbarHostState = snackbarHostState
                    )
                }

            }
            2 -> {
                SettingsScreen(
                    Modifier.padding(innerPadding),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    X9Theme {
        AppNavigationBar()
    }
}
@Composable
fun AppTheme(theme: AppTheme, content: @Composable () -> Unit) {
    when (theme) {
        AppTheme.STANDARD -> X9Theme(darkTheme = false, content = content)
        AppTheme.LIGHT -> X9Theme(darkTheme = false, content = content)
        AppTheme.DARK -> X9Theme(darkTheme = true, content = content)
        AppTheme.RAINBOW -> CustomThemes("Rainbow",content = content)  // Add custom rainbow theme later
    }
}
@Composable
fun GuestBanner(modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${R.string.Banner}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

