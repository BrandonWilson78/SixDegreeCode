package app.sixdegree.network.responses.getlatesttrip


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("account_type")
    var accountType: Int,
    @SerializedName("api_token")
    var apiToken: Any,
    @SerializedName("bio")
    var bio: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("cover_image")
    var coverImage: String,
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("deleted_at")
    var deletedAt: Any,
    @SerializedName("device_token")
    var deviceToken: String,
    @SerializedName("device_token_ios")
    var deviceTokenIos: Any,
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
    @SerializedName("geo_location")
    var geoLocation: Any,
    @SerializedName("geo_location_latitude")
    var geoLocationLatitude: String,
    @SerializedName("geo_location_longitude")
    var geoLocationLongitude: String,
    @SerializedName("home")
    var home: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("last_name")
    var lastName: Any,
    @SerializedName("latitude")
    var latitude: Any,
    @SerializedName("login_type")
    var loginType: String,
    @SerializedName("longitude")
    var longitude: Any,
    @SerializedName("maptype")
    var maptype: String,
    @SerializedName("md5_password")
    var md5Password: Any,
    @SerializedName("mobile")
    var mobile: Any,
    @SerializedName("name")
    var name: String,
    @SerializedName("profile_image")
    var profileImage: String,
    @SerializedName("radius")
    var radius: Any,
    @SerializedName("radius_unit")
    var radiusUnit: String,
    @SerializedName("roles")
    var roles: String,
    @SerializedName("social_id")
    var socialId: Any,
    @SerializedName("status")
    var status: Int,
    @SerializedName("surname")
    var surname: String,
    @SerializedName("temprature")
    var temprature: String,
    @SerializedName("units")
    var units: Any,
    @SerializedName("updated_at")
    var updatedAt: String
)