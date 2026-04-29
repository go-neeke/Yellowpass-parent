package kr.co.yellowpass.parent.network

import kr.co.yellowpass.parent.network.model.BoardingHistory
import kr.co.yellowpass.parent.network.model.Child
import kr.co.yellowpass.parent.network.model.DeviceTokenRequest
import kr.co.yellowpass.parent.network.model.LoginRequest
import kr.co.yellowpass.parent.network.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse
    @POST("/api/device-token")
    suspend fun sendDeviceToken(
        @Body request: DeviceTokenRequest
    ): Response<Unit>

    @GET("/api/boarding/history")
    suspend fun getBoardingHistory(
        @Query("parentId") parentId: Long
    ): List<BoardingHistory>

    @GET("/api/boarding/{boardingId}")
    suspend fun getBoardingDetail(
        @Path("boardingId") boardingId: Long
    ): BoardingHistory

    @GET("/api/children")
    suspend fun getChildren(
        @Query("parentId") parentId: Long
    ): List<Child>

}