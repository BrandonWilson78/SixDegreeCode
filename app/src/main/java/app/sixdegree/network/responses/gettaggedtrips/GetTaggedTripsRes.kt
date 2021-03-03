package app.sixdegree.network.responses.gettaggedtrips


import com.google.gson.annotations.SerializedName

data class GetTaggedTripsRes(
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)