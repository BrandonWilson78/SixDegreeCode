package app.sixdegree.network.responses.signupres


import com.google.gson.annotations.SerializedName

data class SignupResData(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)