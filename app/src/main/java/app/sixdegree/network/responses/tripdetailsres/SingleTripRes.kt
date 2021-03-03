package app.sixdegree.network.responses.tripdetailsres


import com.google.gson.annotations.SerializedName

data class SingleTripRes(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Boolean?
)