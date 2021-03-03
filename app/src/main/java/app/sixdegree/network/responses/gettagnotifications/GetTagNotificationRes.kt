package app.sixdegree.network.responses.gettagnotifications

data class GetTagNotificationRes(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)