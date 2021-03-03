package app.sixdegree.network.responses.sixdegreesearch


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("sixdegreeMatched")
    var sixdegreeMatched: List<SixdegreeMatched>,
    @SerializedName("tripsofMatchedUsers")
    var tripsofMatchedUsers: List<TripsofMatchedUser>
)