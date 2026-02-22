package dk.itu.moapd.x9.mnla_nals

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReportScreen(modifier: Modifier = Modifier) {
    var reportTitle by rememberSaveable  { mutableStateOf("") }
    var reportDescription by rememberSaveable  { mutableStateOf("") }
    var reportType by rememberSaveable  { mutableStateOf("") }
    var reportSeverity by rememberSaveable  { mutableStateOf("") }
    var expanded by rememberSaveable  { mutableStateOf(false) }

    val reportTypes = stringArrayResource(R.array.create_report_types)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.create_report_header),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = reportTitle,
            onValueChange = { reportTitle = it },
            label = { Text(stringResource(id = R.string.create_report_title)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = reportType,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(id = R.string.create_report_type)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                reportTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            reportType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = reportDescription,
            onValueChange = { reportDescription = it },
            label = { Text(stringResource(id = R.string.create_report_description)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { reportSeverity = "Low" },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.create_report_severity_low),
                    fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { reportSeverity = "Mid" },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.create_report_severity_mid),
                    fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { reportSeverity = "High" },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.create_report_severity_high),
                    fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                SubmitReport(reportTitle, reportType, reportDescription, reportSeverity)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.create_report_submit),
                fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun SubmitReport(
    reportTitle: String,
    reportType: String,
    reportDescription: String,
    severity: String
) =
    if (!reportTitle.isEmpty() || !reportDescription.isEmpty()) {
        Log.d(
            "Submit", """
            User report has been submitted with the following information
            Report Title: ${reportTitle}
            Report Type: ${reportType}
            Report Description: ${reportDescription}
            Severity: ${severity}
        """.trimIndent()
        )
//        val intent = Intent(this@CreateReportActivity, MainActivity::class.java)
//        intent.putExtra("REPORT_TITLE", reportTitle)
//        intent.putExtra("REPORT_TYPE", reportType)
//        intent.putExtra("REPORT_DESCRIPTION", reportDescription)
//        intent.putExtra("REPORT_SEVERITY", severity)
//        startActivity(intent)
    } else {
        Log.d(
            "Submit", """
            User report has been submitted with invalid information:
            Report Title: ${reportTitle}
            Report Type: ${reportType}
            Report Description: ${reportDescription}
            Severity: ${severity}
        """.trimIndent()
        )
    }


