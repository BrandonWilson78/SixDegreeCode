package app.sixdegree.network.responses.fetchcommentres


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("comment")
    var comment: String,
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("trip_id")
    var tripId: Int,
    @SerializedName("trip_trail_id")
    var tripTrailId: String,
    @SerializedName("updated_at")
    var updatedAt: String,
    @SerializedName("user_id")
    var userId: Int
)