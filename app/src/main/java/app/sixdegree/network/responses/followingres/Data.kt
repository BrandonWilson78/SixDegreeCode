package app.sixdegree.network.responses.followingres


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("following_name")
    var followingName: String?,
    @SerializedName("following_pic")
    var followingPic: String?,
    @SerializedName("from_user_id")
    var fromUserId: Int?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("location")
    var location: String?,
    @SerializedName("status")
    var status: Int?,
    @SerializedName("to_user_id")
    var toUserId: Int?,
    @SerializedName("updated_at")
    var updatedAt: String?,
    @SerializedName("yourname")
    var yourname: String? ,
    @SerializedName("friend_status")
    var friend_status: String?,
    @SerializedName("follow_status")
    var follow_status: String?
)