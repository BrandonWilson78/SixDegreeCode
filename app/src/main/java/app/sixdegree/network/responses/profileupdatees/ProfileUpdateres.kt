package app.sixdegree.network.responses.profileupdatees


import com.google.gson.annotations.SerializedName

data class ProfileUpdateres(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)