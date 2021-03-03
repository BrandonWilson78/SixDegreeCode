package app.sixdegree.network.responses.gettripdetailsnewres


import com.google.gson.annotations.SerializedName

data class GetTripDetailsResNew(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)