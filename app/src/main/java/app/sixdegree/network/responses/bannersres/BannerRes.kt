package app.sixdegree.network.responses.bannersres


import com.google.gson.annotations.SerializedName

data class BannerRes(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)