package app.sixdegree.network.responses.getSingletripDetails


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("category")
    var category: String,
    @SerializedName("category_info")
    var categoryInfo: CategoryInfo,
    @SerializedName("country")
    var country: String,
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("current_days")
    var currentDays: Int,
    @SerializedName("end_date")
    var endDate: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("latitude")
    var latitude: String,
    @SerializedName("location")
    var location: String,
    @SerializedName("longitude")
    var longitude: String,
    @SerializedName("map_style")
    var mapStyle: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("start_date")
    var startDate: String,
    @SerializedName("status")
    var status: Int,
    @SerializedName("summary")
    var summary: String,
    @SerializedName("tag_ids")
    var tagIds: String,
    @SerializedName("tag_names")
    var tagNames: String,
    @SerializedName("total_days")
    var totalDays: Int,
    @SerializedName("total_distance")
    var totalDistance: Int,
    @SerializedName("total_likes")
    var totalLikes: Int,
    @SerializedName("trails")
    var trails: List<Trail>,
    @SerializedName("travelling")
    var travelling: Boolean,
    @SerializedName("trip_picture")
    var tripPicture: String,
    @SerializedName("updated_at")
    var updatedAt: String,
    @SerializedName("user")
    var user: User,
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("who_can_see")
    var whoCanSee: Int
)