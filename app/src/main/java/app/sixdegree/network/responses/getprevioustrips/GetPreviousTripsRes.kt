package app.sixdegree.network.responses.getprevioustrips


import com.google.gson.annotations.SerializedName

data class GetPreviousTripsRes(
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)