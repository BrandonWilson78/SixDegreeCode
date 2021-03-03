package app.sixdegree.network.responses.content_res


import com.google.gson.annotations.SerializedName

data class ContentRes(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)