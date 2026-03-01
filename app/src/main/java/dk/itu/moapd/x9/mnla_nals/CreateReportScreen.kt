package dk.itu.moapd.x9.mnla_nals

import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalFocusManager
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dk.itu.moapd.x9.mnla_nals.components.AnimatedColorToggleButton
import dk.itu.moapd.x9.mnla_nals.data.Report
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp

@Composable
fun CreateReportScreen(
    modifier: Modifier = Modifier,
    onSubmitReport: (Report) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var reportTitle by rememberSaveable  { mutableStateOf("") }
    var reportDescription by rememberSaveable  { mutableStateOf("") }
    var selectedReportType by rememberSaveable  { mutableStateOf("") }
    var reportSeverity by rememberSaveable  { mutableStateOf("") }

    val reportTypes = stringArrayResource(R.array.create_report_types)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.create_report_header),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = reportTitle,
            onValueChange = { reportTitle = it },
            label = { Text(stringResource(id = R.string.create_report_title)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(32.dp))

        BasicDropdownMenu(
            reportType = selectedReportType,
            reportTypes = reportTypes,
            onTypeSelected = { newType ->
                selectedReportType = newType // This is where the actual reassignment happens
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = reportDescription,
            onValueChange = { reportDescription = it },
            label = { Text(stringResource(id = R.string.create_report_description)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 3,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedColorToggleButton(
                stringResource(id = R.string.create_report_severity_low),
                modifier = Modifier.weight(1f),
                isSelected = reportSeverity == "Low",
                onClick = { reportSeverity = "Low" }
            )
            AnimatedColorToggleButton(
                stringResource(id = R.string.create_report_severity_medium),
                modifier = Modifier.weight(1f),
                isSelected = reportSeverity == "Medium",
                onClick = { reportSeverity = "Medium" }
            )
            AnimatedColorToggleButton(
                stringResource(id = R.string.create_report_severity_high),
                modifier = Modifier.weight(1f),
                isSelected = reportSeverity == "High",
                onClick = { reportSeverity = "High" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if(reportTitle.isNotEmpty() && reportDescription.isNotEmpty() && selectedReportType.isNotEmpty() && reportSeverity.isNotEmpty()) {
                    val report = Report(reportTitle, selectedReportType, reportDescription, reportSeverity)
                    onSubmitReport(report)
                } else {
                    Log.d(
                    "Submit", """
                    User report has been submitted with invalid information:
                    Report Title: ${reportTitle}
                    Report Type: ${selectedReportType}
                    Report Description: ${reportDescription}
                    Severity: ${reportSeverity}
                    """.trimIndent()
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.create_report_submit),
                fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun BasicDropdownMenu(
    reportType: String,         // The current value
    reportTypes: Array<String>,  // The list of options
    onTypeSelected: (String) -> Unit // The callback to update the state
) {
    var expanded by remember { mutableStateOf(false) }

    // Wrap in a Box to provide a coordinate parent for the menu
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = reportType,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(id = R.string.create_report_type)) },
            trailingIcon = {
                // Using a standard IconButton for the toggle
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // This invisible box sits on top of the TextField to catch clicks
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { expanded = !expanded }
        )

        // The standard DropdownMenu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            // This ensures the menu aligns with the start of the Box
            modifier = Modifier.fillMaxWidth(0.9f) // Optional: Adjust width manually
        ) {
            reportTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        // 1. Tell the parent the value changed
                        onTypeSelected(type)
                        // 2. Close the menu
                        expanded = false
                    }
                )
            }
        }
    }
}

