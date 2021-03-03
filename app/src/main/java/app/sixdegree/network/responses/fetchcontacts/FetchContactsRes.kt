package app.sixdegree.network.responses.fetchcontacts


import com.google.gson.annotations.SerializedName

data class FetchContactsRes(
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)