package app.sixdegree.network.responses

data class GetNotificationBadgeCount(
    val message: String,
    val status: Boolean,
    val unreadCount: Int
)