package app.sixdegree.network.responses.fetchcontacts


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id")
    var id: Int,
    @SerializedName("mobile")
    var mobile: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("profile_image")
    var profileImage: String ,
    @SerializedName("status")
    var status: String ,
    @SerializedName("follow_status")
    var follow_status: String,
    @SerializedName("friend_status")
    var friend_status: String
)