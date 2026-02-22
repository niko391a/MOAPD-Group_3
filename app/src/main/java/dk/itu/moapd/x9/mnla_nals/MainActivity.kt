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
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dk.itu.moapd.x9.mnla_nals.ui.theme.X9Theme

// This is for selecting between compose files instead of using fragments and activities
import dk.itu.moapd.x9.mnla_nals.CreateReportScreen
import dk.itu.moapd.x9.mnla_nals.HomeScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        // Start the CreateReportActivity directly
//           Legacy since we now use compose
//        val intent = Intent(this, CreateReportActivity::class.java)
//        startActivity(intent)

        setContent {
            X9Theme {
                AppNavigationBar()
            }
        }
    }
}

@Composable
fun AppNavigationBar() {
    var selectedNavItem by rememberSaveable  { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
            }
        }
    ) { innerPadding ->
        // Content for the selected screen can be placed here
        // For example:
        // This is for selecting between compose files instead of using fragments and activities
        when (selectedNavItem) {
            0 -> {
                HomeScreen()
            }
            1 -> {
                CreateReportScreen(Modifier.padding(innerPadding))
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