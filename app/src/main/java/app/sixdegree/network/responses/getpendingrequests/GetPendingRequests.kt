package app.sixdegree.network.responses.getpendingrequests


import com.google.gson.annotations.SerializedName

data class GetPendingRequests(
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)