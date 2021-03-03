package app.sixdegree.network.responses.getSingletripDetails


import com.google.gson.annotations.SerializedName

data class SingleTripDetailsRes(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)