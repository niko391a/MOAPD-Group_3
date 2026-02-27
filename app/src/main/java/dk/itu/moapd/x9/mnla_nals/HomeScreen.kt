package dk.itu.moapd.x9.mnla_nals

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


@Composable
fun HomeScreen(reports: List<Report>, modifier: Modifier = Modifier, onAddReport: () -> Unit = {}) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
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
                        ReportItem(report)
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAddReport,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color.Blue
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Report",
                tint = Color.White
            )
        }
    }
}

@Composable
fun ReportItem(report: Report) {
    Card(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = report.title, style = MaterialTheme.typography.titleLarge)
            Text(text = "Type: ${report.type}")
            Text(text = "Severity: ${report.severity}")
            Text(text = report.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}