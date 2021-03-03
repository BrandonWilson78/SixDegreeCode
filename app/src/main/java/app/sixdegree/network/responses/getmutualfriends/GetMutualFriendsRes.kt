package app.sixdegree.network.responses.getmutualfriends

data class GetMutualFriendsRes(
    val `data`: Data,
    val message: String,
    val status: Boolean
)