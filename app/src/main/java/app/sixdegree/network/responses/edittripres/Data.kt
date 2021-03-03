package app.sixdegree.network.responses.edittripres

data class Data(
    val category: String,
    val country: Any,
    val created_at: String,
    val end_date: String,
    val id: Int,
    val latitude: Any,
    val location: Any,
    val longitude: Any,
    val map_style: String,
    val name: String,
    val start_date: String,
    val status: String,
    val summary: String,
    val trip_picture: String,
    val updated_at: String,
    val user_id: Int,
    val who_can_see: String
)