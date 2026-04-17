package dk.itu.moapd.x9.mnla_nals.screens

import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalFocusManager
import android.util.Log
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dk.itu.moapd.x9.mnla_nals.R
import dk.itu.moapd.x9.mnla_nals.ViewModels.AuthViewModel
import dk.itu.moapd.x9.mnla_nals.ViewModels.ReportViewModel
import dk.itu.moapd.x9.mnla_nals.ViewModels.SnackViewModel
import dk.itu.moapd.x9.mnla_nals.components.AnimatedColorToggleButton
import dk.itu.moapd.x9.mnla_nals.components.BasicDropdownMenu
import dk.itu.moapd.x9.mnla_nals.data.Report
import kotlinx.coroutines.launch

@Composable
fun CreateReportScreen(
    modifier: Modifier = Modifier,
    reportViewModel: ReportViewModel = viewModel(),
    snackViewModel: SnackViewModel = viewModel(),
    navigate: () -> Unit,
    snackbarHostState: SnackbarHostState,
    authViewModel: AuthViewModel = viewModel(),
    userReport: Report? = null
) {
    val user by authViewModel.user.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var reportTitle by rememberSaveable { mutableStateOf(userReport?.title ?: "") }
    var reportDescription by rememberSaveable { mutableStateOf(userReport?.description ?: "") }
    var selectedReportType by rememberSaveable { mutableStateOf(userReport?.type ?: "") }
    var reportSeverity by rememberSaveable { mutableStateOf(userReport?.severity ?: "") }

    val isEditMode = userReport != null

    val scope = rememberCoroutineScope()
    val reportTypes = stringArrayResource(R.array.create_report_types)
    val notAuthorised = stringResource(R.string.snackbar_No_auth)
    val reportAddSuccess = stringResource(R.string.snackbar_report_add_successful)
    val reportModifySuccess = stringResource(R.string.snackbar_report_modify_successful)
    val reportUnsuccessful = stringResource(R.string.snackbar_report_unsuccessful)

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
            selectedValue = selectedReportType,
            dropdownOptions = reportTypes,
            onTypeSelected = { newType ->
                selectedReportType = newType // This is where the actual reassignment happens
            },
            label = stringResource(id = R.string.create_report_type)
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
                val currentUser = user // local val can be smart cast

                if (currentUser?.isAnonymous == true) {
                    Log.d("auth", "user is not Authorised")
                    scope.launch { snackbarHostState.showSnackbar(notAuthorised) }
                }
                else if (
                    reportTitle.isNotEmpty()
                    && reportDescription.isNotEmpty()
                    && selectedReportType.isNotEmpty()
                    && reportSeverity.isNotEmpty()
                    && currentUser != null
                    ) {

                    val report = Report(
                        uid = currentUser.uid,
                        title = reportTitle,
                        description = reportDescription,
                        type = selectedReportType,
                        severity = reportSeverity
                    )

                    if (isEditMode) {
                        reportViewModel.modifyReport(report)
                        snackViewModel.sendSnackbarMessage(reportModifySuccess)
                    } else {
                        reportViewModel.addReport(report)
                        snackViewModel.sendSnackbarMessage(reportAddSuccess)
                    }

                    navigate()
                } else {
                    // Will be reformatted to use SnackBar in the future
                    Log.d(
                        "Submit", """
                    User report has been submitted with invalid information:
                    Report Title: ${reportTitle}
                    Report Type: ${selectedReportType}
                    Report Description: ${reportDescription}
                    Severity: ${reportSeverity}
                    """.trimIndent()
                    )
                    scope.launch {
                        snackbarHostState.showSnackbar(reportUnsuccessful)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = if (isEditMode) R.string.modify_report_submit else R.string.create_report_submit),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}