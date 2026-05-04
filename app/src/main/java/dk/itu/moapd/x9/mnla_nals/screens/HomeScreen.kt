package dk.itu.moapd.x9.mnla_nals.screens

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import dk.itu.moapd.x9.mnla_nals.R
import dk.itu.moapd.x9.mnla_nals.ViewModels.AuthViewModel
import dk.itu.moapd.x9.mnla_nals.ViewModels.ReportViewModel
import dk.itu.moapd.x9.mnla_nals.ViewModels.SettingsViewModel
import dk.itu.moapd.x9.mnla_nals.components.SeverityPill
import dk.itu.moapd.x9.mnla_nals.data.Report
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onAddReport: () -> Unit = {},
    reportViewModel: ReportViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel()
) {
    // Use by so we can take advantage of Kotlin's inherent get-/setValue
    val currentLocaleTag by settingsViewModel.currentLocaleTag.collectAsState()
    LaunchedEffect(currentLocaleTag) {
        reportViewModel.setLocale(currentLocaleTag)
    }

    val reports by reportViewModel.reports.collectAsState()
    val user by authViewModel.user.collectAsState()
    val sortedReports = reports.sortedByDescending { it.createdAt }
    // track which report the user tapped
    var selectedReport by remember { mutableStateOf<Report?>(null) }

    if (selectedReport != null) {
        ReportDetailScreen(
            selectedReport = selectedReport!!,
            navigate = { selectedReport = null }, // back button clears it
            modifier = modifier,
            onAddReport = onAddReport,
        )
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp,32.dp,0.dp,0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.home_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                if (!reports.isEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(sortedReports) { report ->
                            ReportItem(report, reportViewModel, authViewModel, onAddReport, onReportClick = { selectedReport = it })
                        }
                    }
                }
            }
            if(user?.isAnonymous == false) {
                FloatingActionButton(
                    onClick = onAddReport,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),

                    ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Report",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ReportItem(
    report: Report,
    reportViewModel: ReportViewModel,
    authViewModel: AuthViewModel,
    onAddReport: () -> Unit,
    onReportClick: (Report) -> Unit
) {
    val user by authViewModel.user.collectAsState()
    val timeSinceUploaded = DateUtils.getRelativeTimeSpanString(
        report.createdAt,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS
    ).toString()


    Box(modifier = Modifier.fillMaxWidth()) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                // Extra top padding so the title text doesn't sit under the X button
                modifier = Modifier
                    .padding(start = 16.dp, end = 40.dp, top = 8.dp, bottom = 16.dp)
                    .clickable { onReportClick(report) },
                verticalArrangement = Arrangement.spacedBy(4.dp), // adds 4dp between every child
            ) {
              Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Time since report was created",
                    modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                )
                Text(
                    text = timeSinceUploaded,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, top = 12.dp)
                )
            }
                Text(text = report.title, style = MaterialTheme.typography.titleLarge)
                Text(text = stringResource(id = R.string.home_report_type) + ": ${report.type}")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(id = R.string.home_report_severity) + ": ")
                    SeverityPill(severity = report.severity)
                }
                // User can view report description in report details view, so for better UX this has been commented out
//                Text(text = report.description, style = MaterialTheme.typography.bodyMedium)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}