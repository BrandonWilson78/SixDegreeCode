package app.sixdegree.network.responses.viewfriends

data class Data(
    val created_at: String,
    val current_days: Int,
    val follow_status: String,
    val from_user_id: Int,
    val id: Int,
    val location: String,
    val name: String,
    val profile_image: String,
    val status: Int,
    val to_user_id: Int,
    val total_days: Int,
    val total_distance: Double,
    val total_likes: Int,
    val travelling: Boolean,
    val trip_name: String,
    val trip_picture: String,
    val updated_at: String
)