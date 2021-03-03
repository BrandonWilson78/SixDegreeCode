package app.sixdegree.model.login


import com.google.gson.annotations.SerializedName

data class LoginResponseData(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)