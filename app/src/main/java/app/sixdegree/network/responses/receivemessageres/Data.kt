package app.sixdegree.network.responses.receivemessageres

data class Data(
    val conversation_id: Int,
    val created_at: String,
    val file_name: String,
    val from_user_id: Int,
    val id: Int,
    val message: String,
    val seen: Int,
    val to_user_id: Int,
    val updated_at: String
)