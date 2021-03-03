package app.sixdegree.network.responses.showlikeslist

data class Data(
    val first_user_image: String,
    val first_user_name: String,
    val otherCount: Int,
    val second_user_image: String,
    val second_user_name: String
)