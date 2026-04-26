package dk.itu.moapd.x9.mnla_nals.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.util.UUID

class StorageRepository {
    private val storage = Firebase.storage.reference

    fun uploadReportImage(
        uri: Uri,
        onSuccess: (downloadUrl: String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Each image gets a unique name to avoid collisions
        val imageRef = storage.child("report_images/${UUID.randomUUID()}.jpg")

        imageRef.putFile(uri)
            .addOnSuccessListener {
                // putFile succeeds but we need a separate call to get the public URL
                imageRef.downloadUrl
                    .addOnSuccessListener { uri -> onSuccess(uri.toString()) }
                    .addOnFailureListener(onFailure)
            }
            .addOnFailureListener(onFailure)
    }
}