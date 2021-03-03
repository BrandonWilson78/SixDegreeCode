package app.sixdegree.network.responses.getsingleTrailDetails

data class Data(
    val category: Category,
    val country: String,
    val created_at: String,
    val end_date: String,
    val id: Int,
    val latitude: String,
    val location: String,
    val longitude: String,
    val map_style: String,
    val name: String,
    val start_date: String,
    val status: Int,
    val summary: String,
    val trail_pictures: String,
    val trip_id: Int,
    val updated_at: String,
    val user: User,
    val user_id: Int,
    val who_can_see: Int,
    val like_status: Int
)