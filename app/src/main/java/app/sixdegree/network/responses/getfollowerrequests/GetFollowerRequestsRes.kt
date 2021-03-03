package app.sixdegree.network.responses.getfollowerrequests

data class GetFollowerRequestsRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)