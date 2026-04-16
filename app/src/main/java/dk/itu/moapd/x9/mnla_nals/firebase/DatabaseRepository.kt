package dk.itu.moapd.x9.mnla_nals.firebase

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.*
import dk.itu.moapd.x9.mnla_nals.data.Report
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DatabaseRepository {
    private val database: DatabaseReference = Firebase.database.reference

    /**
     * Pushes a report object tot he real time database
     * @param report the report to be added.
     */
    fun addReport(
        report: Report,
    ) {
        database.child("reports").push().setValue(report)
        Log.d("Info", "Report added successfully to database")
    }

    /**
     * Deletes a report from the realtime database based on an id
     * @param reportId the id of the report to be deleted.
     */
    fun deleteReport(reportId: String) {
        database.child("reports").child(reportId).removeValue()
    }

    /**
     * Returns a Flow that emits a fresh list whenever the reports node changes.
     * We keep uid-scoped paths since that's how you're currently writing data —
     * all reports live under reports/
     */
    fun getReportsFlow(): Flow<List<Report>> = callbackFlow {
        val ref = database.child("reports")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reports = snapshot.children.mapNotNull { child ->
                    // Use the firebase generated key as the reports id
                    child.getValue(Report::class.java)?.copy(id = child.key ?: "")
                }
                trySend(reports)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        Log.d("Info", "Report retrieved successfully")
        ref.addValueEventListener(listener)

        // Removes the listener when the Flow is cancelled (e.g. ViewModel cleared)
        awaitClose { ref.removeEventListener(listener) }
    }
}
