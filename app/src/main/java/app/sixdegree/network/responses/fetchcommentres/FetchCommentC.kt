package app.sixdegree.network.responses.fetchcommentres


import com.google.gson.annotations.SerializedName

data class FetchCommentC(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)