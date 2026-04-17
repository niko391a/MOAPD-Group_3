package dk.itu.moapd.x9.mnla_nals.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.x9.mnla_nals.data.Report
import dk.itu.moapd.x9.mnla_nals.firebase.DatabaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn


class ReportViewModel : ViewModel() {
    val db = DatabaseRepository()
    private val _reportToEdit = MutableStateFlow<Report?>(null)
    val reportToEdit: StateFlow<Report?> = _reportToEdit.asStateFlow()
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

    fun removeReport(report: Report) {
        db.removeReport(report)
        Log.d("info", "Removed report: $report")
    }

    fun modifyReport(report: Report) {
        db.modifyReport(report)
        Log.d("info", "Modified report; $report")
    }

    fun setReportToEdit(report: Report?) {
        _reportToEdit.value = report
    }
}