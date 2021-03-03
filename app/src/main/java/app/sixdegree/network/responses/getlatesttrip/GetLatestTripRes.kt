package app.sixdegree.network.responses.getlatesttrip


import com.google.gson.annotations.SerializedName

data class GetLatestTripRes(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)