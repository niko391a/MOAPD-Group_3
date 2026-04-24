package dk.itu.moapd.x9.mnla_nals.screens

import android.app.LocaleManager
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseUser
import dk.itu.moapd.x9.mnla_nals.R
import dk.itu.moapd.x9.mnla_nals.ViewModels.AuthViewModel
import dk.itu.moapd.x9.mnla_nals.ViewModels.SettingsViewModel
import dk.itu.moapd.x9.mnla_nals.components.AnimatedColorToggleButton
import dk.itu.moapd.x9.mnla_nals.components.BasicDropdownMenu
import dk.itu.moapd.x9.mnla_nals.data.Report
import dk.itu.moapd.x9.mnla_nals.ui.theme.AppTheme
import java.util.Locale

@Composable
fun ReportDetailScreen(selectedReport: Report) {

}

@Composable
fun MapData() {

}

@Composable
fun ImageData() {

}

@Composable
fun ReportData() {
    
}