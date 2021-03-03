package app.sixdegree.network.responses.sixdegreesearch


import com.google.gson.annotations.SerializedName

data class SixdegreeMatched(
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("follow_status")
    var followStatus: String,
    @SerializedName("friend_status")
    var friend_status: String,
    @SerializedName("home")
    var home: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("mutual_friends")
    var mutualFriends: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("profile_image")
    var profileImage: String,
    @SerializedName("relation")
    var relation: String,
    @SerializedName("surname")
    var surname: String
)