package app.sixdegree.network.responses.getpendingrequests


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("follow_status")
    var followStatus: String,
    @SerializedName("follower_name")
    var followerName: String,
    @SerializedName("follower_pic")
    var followerPic: String,
    @SerializedName("friend_status")
    var friendStatus: String,
    @SerializedName("from_user_id")
    var fromUserId: Int,
    @SerializedName("home_location")
    var homeLocation: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("status")
    var status: Int,
    @SerializedName("to_user_id")
    var toUserId: Int,
    @SerializedName("updated_at")
    var updatedAt: String,
    @SerializedName("yourname")
    var yourname: String
)