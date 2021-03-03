package app.sixdegree.network.responses.getprevioustrips


import com.google.gson.annotations.SerializedName

data class Trail(
    @SerializedName("category")
    var category: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("end_date")
    var endDate: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("latitude")
    var latitude: String,
    @SerializedName("location")
    var location: Any,
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
    @SerializedName("trail_pictures")
    var trailPictures: String,
    @SerializedName("trip_id")
    var tripId: Int,
    @SerializedName("updated_at")
    var updatedAt: Any,
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("who_can_see")
    var whoCanSee: Int
)