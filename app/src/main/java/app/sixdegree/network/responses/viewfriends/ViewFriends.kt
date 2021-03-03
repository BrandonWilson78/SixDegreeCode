package app.sixdegree.network.responses.viewfriends

data class ViewFriends(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)