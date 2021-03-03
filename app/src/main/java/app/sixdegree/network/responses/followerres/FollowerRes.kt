package app.sixdegree.network.responses.followerres


import com.google.gson.annotations.SerializedName

data class FollowerRes(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Boolean?
)