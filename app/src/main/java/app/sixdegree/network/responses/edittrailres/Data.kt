package app.sixdegree.network.responses.edittrailres

data class Data(
    val category: String,
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
    val status: String,
    val summary: String,
    val trail_pictures: String,
    val trip_id: String,
    val updated_at: String,
    val user_id: Int,
    val who_can_see: String
)