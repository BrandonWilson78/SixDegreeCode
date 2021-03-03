package app.sixdegree.network.responses.settings_mod.profile


import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)