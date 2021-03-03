package app.sixdegree.network.responses.gettagnotifications

data class Data(
    val created_at: String,
    val `data`: String,
    val id: Int,
    val notifiable_id: Int,
     val notifiable_type: Any,
    val notification_by: Int,
    val profile_image: String,
    val read_at: Any,
    val trail_id: Any,
    val trip_id: String,
    val trip_name: String,
    val trip_picture: String,
    val type: Any,
    val updated_at: String,
    val conversation_id: String
)