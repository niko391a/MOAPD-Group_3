package dk.itu.moapd.x9.mnla_nals.ViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dk.itu.moapd.x9.mnla_nals.data.Report
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReportViewModel : ViewModel() {
    // Expose screen UI state
    private val reportList =  mutableStateListOf<Report>()
    private val _uiState = MutableStateFlow(reportList)
    val exposedReportList: StateFlow<List<Report>> = _uiState.asStateFlow()

    fun addReport(report: Report) {
        reportList.add(report)
    }

}