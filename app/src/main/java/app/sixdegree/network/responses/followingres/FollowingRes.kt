package app.sixdegree.network.responses.followingres


import com.google.gson.annotations.SerializedName

data class FollowingRes(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Boolean?
)