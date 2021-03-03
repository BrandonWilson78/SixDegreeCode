package app.sixdegree.network.responses.getfollowerrequests

data class Data(
    val created_at: String,
    val follower_name: String,
    val follower_pic: String,
    val from_user_id: Int,
    val home_location: String,
    val id: Int,
    val status: Int,
    val to_user_id: Int,
    val updated_at: String,
    val yourname: String,
    val friend_status: String,
    val follow_status: String
)