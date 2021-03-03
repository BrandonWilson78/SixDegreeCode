package app.sixdegree.network.responses.getinboxres

data class Data(
    val conversation_id: Int,
    val from_nam: String,
    val from_name: String,
    val from_user_id: Int,
    val from_user_picture: String,
    val id: Int,
    val last_message: String,
    val last_message_time: String,
    val unseen_messages_count: Int
)