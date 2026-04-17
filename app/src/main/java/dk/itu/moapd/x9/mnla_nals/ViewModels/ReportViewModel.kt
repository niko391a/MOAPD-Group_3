package dk.itu.moapd.x9.mnla_nals.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.x9.mnla_nals.data.Report
import dk.itu.moapd.x9.mnla_nals.firebase.DatabaseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn


class ReportViewModel : ViewModel() {
    val db = DatabaseRepository()

//    private val reportList =  mutableStateListOf<Report>()
//    private val _uiState = MutableStateFlow(reportList)
//    val exposedReportList: StateFlow<List<Report>> = _uiState.asStateFlow()

    val reports: StateFlow<List<Report>> = db.getReportsFlow()
        .catch { e -> Log.e("ReportViewModel", "Reports flow error: ${e.message}") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addReport(report: Report) {
        db.addReport(report)
        Log.d("info", "Added report: $report")
    }

    fun removeReport(reportId: String) {
        db.removeReport(reportId)
        Log.d("info", "Removed report: $reportId")
    }
}