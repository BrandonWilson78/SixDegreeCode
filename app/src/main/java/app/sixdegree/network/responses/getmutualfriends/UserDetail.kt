package app.sixdegree.network.responses.getmutualfriends

data class UserDetail(
    val country_visited: String,
    val created_at: String,
    val id: Int,
    val interests: String,
    val total_days: String,
    val total_hours: String,
    val total_km: String,
    val total_trails: String,
    val updated_at: String,
    val user_id: Int
)