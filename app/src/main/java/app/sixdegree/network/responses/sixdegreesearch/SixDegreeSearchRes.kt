package app.sixdegree.network.responses.sixdegreesearch


import com.google.gson.annotations.SerializedName

data class SixDegreeSearchRes(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)