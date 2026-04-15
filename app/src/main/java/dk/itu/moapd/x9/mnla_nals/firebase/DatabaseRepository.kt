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

    fun addReport(
        report: Report,
        uid: String
    ) {
        database.child("reports").child(uid).push().setValue(report)
        Log.d("Info", "Report added successfully to database")
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
