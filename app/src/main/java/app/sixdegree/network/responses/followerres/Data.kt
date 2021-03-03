package app.sixdegree.network.responses.followerres


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("follower_name")
    var followerName: String?,
    @SerializedName("follower_pic")
    var followerPic: String?,
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
    var updatedAt: Any?,
    @SerializedName("yourname")
    var yourname: String?,
    @SerializedName("friend_status")
    var friend_status: String?,
    @SerializedName("follow_status")
    var follow_status: String?
)