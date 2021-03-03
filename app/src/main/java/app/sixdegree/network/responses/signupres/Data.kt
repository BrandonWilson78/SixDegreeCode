package app.sixdegree.network.responses.signupres


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("email_verification_code")
    var emailVerificationCode: String,
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("maptype")
    var maptype: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("profile_image")
    var profileImage: String,
    @SerializedName("radius_unit")
    var radiusUnit: String,
    @SerializedName("roles")
    var roles: Int,
    @SerializedName("status")
    var status: Int,
    @SerializedName("surname")
    var surname: String,
    @SerializedName("temprature")
    var temprature: String,
    @SerializedName("updated_at")
    var updatedAt: String
)