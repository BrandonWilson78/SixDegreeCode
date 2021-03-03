package app.sixdegree.network.responses.sendmessage

data class Data(
    val conversation_id: String,
    val from_user_id: String,
    val message: String,
    val to_user_id: String
)