package app.sixdegree.network.responses.fetchcomments

data class FetchCommentsRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)