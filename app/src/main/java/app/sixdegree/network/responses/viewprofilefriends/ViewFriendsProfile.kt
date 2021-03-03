package app.sixdegree.network.responses.viewprofilefriends


import com.google.gson.annotations.SerializedName

data class ViewFriendsProfile(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)