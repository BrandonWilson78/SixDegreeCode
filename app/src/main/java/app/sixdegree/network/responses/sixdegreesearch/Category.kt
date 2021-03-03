package app.sixdegree.network.responses.sixdegreesearch


import com.google.gson.annotations.SerializedName

data class Category(
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
    @SerializedName("updated_at")
    var updatedAt: String
)