package app.sixdegree.network.responses.sendmessage

data class SendMessageRes(
    val `data`: Data,
    val message: String,
    val status: Boolean
)