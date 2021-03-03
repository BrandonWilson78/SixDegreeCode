package app.sixdegree.network.responses.gettripcategories


import com.google.gson.annotations.SerializedName

data class GetTripCategories(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Boolean?
)