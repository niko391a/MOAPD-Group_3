package dk.itu.moapd.x9.mnla_nals.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dk.itu.moapd.x9.mnla_nals.data.Report
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import dk.itu.moapd.x9.mnla_nals.R
import dk.itu.moapd.x9.mnla_nals.ViewModels.AuthViewModel
import dk.itu.moapd.x9.mnla_nals.ViewModels.ReportViewModel


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onAddReport: () -> Unit = {},
    reportViewModel: ReportViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
) {
    // Use by so we can take advantage of Kotlins inherent get-/setValue
    val reports by reportViewModel.reports.collectAsState(initial = emptyList())
    val user by authViewModel.user.collectAsState()
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
                    items(reports) { report ->
                        ReportItem(report, reportViewModel)
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

@Composable
fun ReportItem(
    report: Report,
    reportViewModel: ReportViewModel,
    authViewModel: AuthViewModel = viewModel(),
) {
    val user by authViewModel.user.collectAsState()

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                // Extra top padding so the title text doesn't sit under the X button
                modifier = Modifier.padding(start = 16.dp, end = 40.dp, top = 8.dp, bottom = 16.dp)
            ) {
                Text(text = report.title, style = MaterialTheme.typography.titleLarge)
                Text(text = "Type: ${report.type}")
                Text(text = "Severity: ${report.severity}")
                Text(text = report.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
        if (report.uid == user?.uid) {
            IconButton(
                onClick = { reportViewModel.removeReport(report.id) },
                modifier = Modifier.align(Alignment.TopEnd) // pins to upper-right of the Box
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete report"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}