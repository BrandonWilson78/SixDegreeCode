package app.sixdegree.network.responses.exlporeres


import com.google.gson.annotations.SerializedName

data class ExlporeListRes(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Boolean?
)