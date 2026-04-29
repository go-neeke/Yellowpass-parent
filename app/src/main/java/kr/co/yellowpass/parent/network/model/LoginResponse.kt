package kr.co.yellowpass.parent.network.model

import kr.co.yellowpass.parent.network.Role

data class LoginResponse(
    val userId: Long,
    val name: String,
    val role: Role
)