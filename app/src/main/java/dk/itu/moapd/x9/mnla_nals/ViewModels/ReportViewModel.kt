package dk.itu.moapd.x9.mnla_nals.ViewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.x9.mnla_nals.data.Report
import dk.itu.moapd.x9.mnla_nals.firebase.DatabaseRepository
import dk.itu.moapd.x9.mnla_nals.firebase.StorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn


class ReportViewModel : ViewModel() {
    private val db = DatabaseRepository()
    private val storage = StorageRepository()
    private val _reportToEdit = MutableStateFlow<Report?>(null)
    val reportToEdit: StateFlow<Report?> = _reportToEdit.asStateFlow()
    private val _localeTag = MutableStateFlow("en")

    var lastReportTimeMs: Long = 0L

    val reports: StateFlow<List<Report>> = _localeTag
        .flatMapLatest { locale ->
            db.getReportsFlow(locale)
                .catch { e -> Log.e("ReportViewModel", "Reports flow error: ${e.message}") }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun setLocale(localeTag: String) {
        _localeTag.value = localeTag
    }

    fun addReport(report: Report) {
        db.addReport(report)
        Log.d("info", "Added report: $report")
    }

    fun addReportWithImage(report: Report, imageUri: Uri?) {
        if (imageUri == null) {
            // No image selected — save immediately
            addReport(report)
            return
        }

        storage.uploadReportImage(
            uri = imageUri,
            onSuccess = { downloadUrl ->
                addReport(report.copy(imageUrl = downloadUrl))
            },
            onFailure = { e ->
                Log.e("ReportViewModel", "Image upload failed: ${e.message}")
            }
        )
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