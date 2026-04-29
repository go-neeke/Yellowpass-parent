package kr.co.yellowpass.parent.network.model

import kr.co.yellowpass.parent.network.Role

data class LoginRequest(
    val username: String?,
    val phone: String?,
    val password: String,
    val role: Role
)