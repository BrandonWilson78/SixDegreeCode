package app.sixdegree.network.responses.getfriendslisting


import com.google.gson.annotations.SerializedName

data class GetFriendsListing(
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)