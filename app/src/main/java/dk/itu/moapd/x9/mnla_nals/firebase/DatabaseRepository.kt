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

    private fun DatabaseError.toUserMessage(): String = when (code) {
        DatabaseError.PERMISSION_DENIED -> "Permission denied. You may not have access to this data."
        DatabaseError.NETWORK_ERROR     -> "Network error. Please check your connection."
        DatabaseError.OPERATION_FAILED  -> "Operation failed. Please try again."
        DatabaseError.DISCONNECTED      -> "Disconnected from the database."
        else                            -> "Database error: $message"
    }

    /**
     * Pushes a report object to the real time database.
     * @param report the report to be added.
     */
    fun addReport(report: Report) {
        database.child("reports").push().setValue(report)
            .addOnSuccessListener {
                Log.d(TAG, "Report added successfully")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to add report: ${e.message}")
            }
    }

    /**
     * Removes a report from the realtime database based on an id.
     * @param reportId the id of the report to be removed.
     */
    fun removeReport(report: Report) {
        database.child("reports").child(report.id).removeValue()
            .addOnSuccessListener {
                Log.d(TAG, "Report $report removed successfully")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to remove report $report: ${e.message}")
            }
    }

    /**
     * Modifies a report from the realtime database based on an id.
     * @param report the report to be modified.
     */
    fun modifyReport(report: Report) {
        database.child("reports").child(report.id).setValue(report)
            .addOnSuccessListener {
                Log.d(TAG, "Report modified successfully")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to modify report: ${e.message}")
            }
    }
    /**
     * Returns a Flow that emits a fresh list whenever the reports node changes.
     * All reports live under reports/
     */
    fun getReportsFlow(localeTag: String): Flow<List<Report>> = callbackFlow {
        val ref = database.child("reports").orderByChild("language").equalTo(localeTag)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reports = snapshot.children.mapNotNull { child ->
                    // Use the Firebase-generated key as the report's id
                    child.getValue(Report::class.java)?.copy(id = child.key ?: "")
                }
                trySend(reports)
            }

            override fun onCancelled(error: DatabaseError) {
                val message = error.toUserMessage()
                Log.e(TAG, "Listener cancelled (code=${error.code}): $message")
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)

        // Removes the listener when the Flow is cancelled (e.g. ViewModel cleared)
        awaitClose { ref.removeEventListener(listener) }
    }

    companion object {
        private const val TAG = "DatabaseRepository"
    }
}
