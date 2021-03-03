package app.sixdegree.network.responses.fetchcomments

data class Data(
    val comment: String,
    val created_at: String,
    val id: Int,
    val trip_id: Int,
    val trip_trail_id: Int,
    val updated_at: String,
    val user: User,
    val user_id: Int
)