package kr.co.yellowpass.parent.network.model

data class DeviceTokenRequest(
    val parentId: Long,
    val fcmToken: String
)