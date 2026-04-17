package dk.itu.moapd.x9.mnla_nals.data

import com.google.firebase.database.Exclude

data class Report(
    /*
    * We signal to Firebase to Exclude the `id` field this is due to us only needing the id for
    * establishing a relation between the key stored in firebase and the objects we interact with
    * in Kotlin therefore if we were to not ignore it then it would create redundant noise in the data tab
    */
    @get:Exclude val id: String = "",
    val uid: String = "",
    val title: String = "",
    val description: String = "",
    val type: String = "",
    val severity: String = "",
    val createdAt: Long = System.currentTimeMillis()
)