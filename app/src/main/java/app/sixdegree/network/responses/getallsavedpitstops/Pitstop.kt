package app.sixdegree.network.responses.getallsavedpitstops


import com.google.gson.annotations.SerializedName

data class Pitstop(
    @SerializedName("bookmark_id")
    val bookmarkId: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("category_image")
    val categoryImage: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("liked_by_you")
    val likedByYou: Boolean,
    @SerializedName("location")
    val location: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("map_style")
    val mapStyle: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("pitstop_picture")
    val pitstopPicture: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("total_comments")
    val totalComments: Int,
    @SerializedName("total_likes")
    val totalLikes: Int,
    @SerializedName("trail_id")
    val trailId: Int,
    @SerializedName("trip_id")
    val tripId: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("who_can_see")
    val whoCanSee: Int
)