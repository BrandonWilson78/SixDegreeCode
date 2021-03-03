package app.sixdegree.network.responses.getSingletripDetails


import com.google.gson.annotations.SerializedName

data class CategoryInfo(
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("picture")
    var picture: String,
    @SerializedName("type")
    var type: Int,
    @SerializedName("updated_at")
    var updatedAt: String
)