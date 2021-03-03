package app.sixdegree.network.responses.viewprofilefriends


import com.google.gson.annotations.SerializedName

data class UserDetail(
    @SerializedName("country_visited")
    var countryVisited: Any,
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("interests")
    var interests: String,
    @SerializedName("total_days")
    var totalDays: Any,
    @SerializedName("total_hours")
    var totalHours: Any,
    @SerializedName("total_km")
    var totalKm: Any,
    @SerializedName("total_trails")
    var totalTrails: Any,
    @SerializedName("updated_at")
    var updatedAt: String,
    @SerializedName("user_id")
    var userId: Int
)