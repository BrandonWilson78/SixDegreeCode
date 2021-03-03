package app.sixdegree.network.responses.viewprofilefriends


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("account_type")
    var accountType: Int,
    @SerializedName("api_token")
    var apiToken: Any,
    @SerializedName("bio")
    var bio: String,
    @SerializedName("countries_visited_name")
    var countriesVisitedName: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("cover_image")
    var coverImage: String,
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("current_days")
    var currentDays: Int,
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
    @SerializedName("follow_status")
    var followStatus: String,
    @SerializedName("followers")
    var followers: Int,
    @SerializedName("followings")
    var followings: Int,
    @SerializedName("friend_status")
    var friendStatus: String,
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
    @SerializedName("latest_trip")
    var latestTrip: LatestTrip,
    @SerializedName("latitude")
    var latitude: String,
    @SerializedName("login_type")
    var loginType: String,
    @SerializedName("longitude")
    var longitude: String,
    @SerializedName("maptype")
    var maptype: String,
    @SerializedName("md5_password")
    var md5Password: Any,
    @SerializedName("mobile")
    var mobile: String,
    @SerializedName("mutual_friends")
    var mutualFriends: List<MutualFriend>,
    @SerializedName("mutual_friends_count")
    var mutualFriendsCount: Int,
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
    @SerializedName("total_days")
    var totalDays: Int,
    @SerializedName("total_distance")
    var totalDistance: Int,
    @SerializedName("total_likes")
    var totalLikes: Int,
    @SerializedName("trail")
    var trail: Trail,
    @SerializedName("travelling")
    var travelling: Boolean,
    @SerializedName("trips_exists")
    var tripsExists: Boolean,
    @SerializedName("units")
    var units: Any,
    @SerializedName("updated_at")
    var updatedAt: String,
    @SerializedName("userDetail")
    var userDetail: UserDetail
)