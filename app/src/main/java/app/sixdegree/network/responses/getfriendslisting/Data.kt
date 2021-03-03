package app.sixdegree.network.responses.getfriendslisting


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("mutualFriends")
    var mutualFriends: String,
    @SerializedName("current_days")
    var currentDays: Int,
    @SerializedName("friend_id")
    var friendId: Int,
    @SerializedName("friend_image")
    var friendImage: String,
    @SerializedName("friend_latitude")
    var friendLatitude: String,
    @SerializedName("friend_longitude")
    var friendLongitude: String,
    @SerializedName("friend_name")
    var friendName: String,
    @SerializedName("from_user_id")
    var fromUserId: Int,
    @SerializedName("id")
    var id: Int,
    @SerializedName("location")
    var location: String,
    @SerializedName("status")
    var status: Int,
    @SerializedName("to_user_id")
    var toUserId: Int,
    @SerializedName("total_days")
    var totalDays: Int,
    @SerializedName("total_distance")
    var totalDistance: Double,
    @SerializedName("total_likes")
    var totalLikes: Int,
    @SerializedName("travelling")
    var travelling: Boolean,
    @SerializedName("trip_name")
    var tripName: String,
    @SerializedName("trip_picture")
    var tripPicture: String,
    @SerializedName("updated_at")
    var updatedAt: String ,
        @SerializedName("home_location")
    var home_location: String
)