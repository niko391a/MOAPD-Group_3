package dk.itu.moapd.x9.mnla_nals.data

data class Report(
    val id: String = "",
    val uid: String = "",
    val title: String = "",
    val description: String = "",
    val type: String = "",
    val severity: String = "",
    val createdAt: Long = System.currentTimeMillis()
)