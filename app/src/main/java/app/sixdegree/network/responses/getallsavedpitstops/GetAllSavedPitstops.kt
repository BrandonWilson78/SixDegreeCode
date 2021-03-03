package app.sixdegree.network.responses.getallsavedpitstops


import com.google.gson.annotations.SerializedName

data class GetAllSavedPitstops(
    @SerializedName("message")
    val message: String,
    @SerializedName("pitstops")
    val pitstops: List<Pitstop>,
    @SerializedName("status")
    val status: Boolean
)