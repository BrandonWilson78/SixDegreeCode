package app.sixdegree.network.responses.getinboxres

data class GetInboxRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)