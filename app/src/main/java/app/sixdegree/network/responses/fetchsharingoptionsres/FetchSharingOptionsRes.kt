package app.sixdegree.network.responses.fetchsharingoptionsres


import com.google.gson.annotations.SerializedName

data class FetchSharingOptionsRes(
    @SerializedName("data")
    var `data`: List<String>,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)