package dk.itu.moapd.x9.mnla_nals.ViewModels

// /home/matt/DEV/MOAPD-Group_3/app/src/main/java/dk/itu/moapd/x9/mnla_nals/ViewModels/ReportViewModel.kt


import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.database
import dk.itu.moapd.x9.mnla_nals.data.Report

class ReportViewModel : ViewModel() {
    // Reference to your Realtime Database
    private val database = Firebase.database.reference.child("reports")

    fun addReport(report: Report) {
        // Push creates a unique ID for each report
        database.push().setValue(report)
    }
}