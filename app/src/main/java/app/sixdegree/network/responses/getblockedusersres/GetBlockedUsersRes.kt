package app.sixdegree.network.responses.getblockedusersres

data class GetBlockedUsersRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)