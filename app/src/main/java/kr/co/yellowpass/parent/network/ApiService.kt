package kr.co.yellowpass.parent.network

import kr.co.yellowpass.parent.network.model.DeviceTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/api/device-token")
    suspend fun sendDeviceToken(
        @Body request: DeviceTokenRequest
    ): Response<Unit>
}