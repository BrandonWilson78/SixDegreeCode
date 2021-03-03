package app.sixdegree.network.responses.viewprofilefriends


import com.google.gson.annotations.SerializedName

data class MutualFriend(
    @SerializedName("email")
    var email: String,
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("home")
    var home: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("profile_image")
    var profileImage: String,
    @SerializedName("surname")
    var surname: String
)