package app.sixdegree.network.responses.tripsinterest


import com.google.gson.annotations.SerializedName

data class InterestTripRes(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Boolean?
)