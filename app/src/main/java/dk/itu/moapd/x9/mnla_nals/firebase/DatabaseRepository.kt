package dk.itu.moapd.x9.mnla_nals.firebase

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import dk.itu.moapd.x9.mnla_nals.data.Report

class DatabaseRepository {
    private val database: DatabaseReference = Firebase.database.reference

    fun addReport(
        report: Report,
        uid: String
    ) {

        database.child("reports").child(uid).push().setValue(report)
        Log.d("Info", "Report added successfully to database")
    }


    fun retriveReport() {
        database.child("reports")
    }
}