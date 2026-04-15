package dk.itu.moapd.x9.mnla_nals.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dk.itu.moapd.x9.mnla_nals.data.Report
import dk.itu.moapd.x9.mnla_nals.firebase.DatabaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class ReportViewModel : ViewModel() {
    // Expose screen UI state
    private val reportList =  mutableStateListOf<Report>()
    private val _uiState = MutableStateFlow(reportList)
    val exposedReportList: StateFlow<List<Report>> = _uiState.asStateFlow()
    val db = DatabaseRepository()

    fun addReport(report: Report, uid: String) {
        db.addReport(report, uid)
        reportList.add(report)
        Log.d("info", "Added report: $report")
    }

    fun getReportList() {
        db.retriveReport()
    }

}