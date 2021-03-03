package app.sixdegree.network.responses


import com.google.gson.annotations.SerializedName

data class BaseRes(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)