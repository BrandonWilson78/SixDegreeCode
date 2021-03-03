package app.sixdegree.model.login


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("account_type")
    var accountType: Int,
    @SerializedName("country")
    var country: String,
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("email_verification_code")
    var emailVerificationCode: String,
    @SerializedName("email_verified")
    var emailVerified: Int,
    @SerializedName("email_verified_at")
    var emailVerifiedAt: String,
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("home")
    var home: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("interest_added")
    var interestAdded: Boolean,
    @SerializedName("latitude")
    var latitude: String,
    @SerializedName("login_type")
    var loginType: String,
    @SerializedName("longitude")
    var longitude: String,
    @SerializedName("maptype")
    var maptype: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("profile_image")
    var profileImage: String,
    @SerializedName("radius_unit")
    var radiusUnit: String,
    @SerializedName("role_name")
    var roleName: String,
    @SerializedName("roles")
    var roles: String,
    @SerializedName("social_id")
    var socialId: String,
    @SerializedName("status")
    var status: Int,
    @SerializedName("surname")
    var surname: String,
    @SerializedName("temprature")
    var temprature: String,
    @SerializedName("token")
    var token: String,
    @SerializedName("updated_at")
    var updatedAt: String
)