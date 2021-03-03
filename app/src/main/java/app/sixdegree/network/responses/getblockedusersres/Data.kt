package app.sixdegree.network.responses.getblockedusersres

data class Data(
    val blocked_user_id: Int,
    val email: String,
    val from_user_id: Int,
    val home: String,
    val latitude: Any,
    val longitude: Any,
    val name: String,
    val profile_image: String
)