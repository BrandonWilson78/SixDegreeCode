package app.sixdegree.network.responses.receivemessageres

data class ReceiveMsgRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)