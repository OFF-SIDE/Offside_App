package com.example.off_side_app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

class ReserveConnectionApi {

    companion object {
        private const val BASE_URL = "http://13.209.73.252:8080"

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)  // API의 기본 URL
            .addConverterFactory(GsonConverterFactory.create())  // JSON 변환기
            .build()

        val reserveApi: ReserveApi = retrofit.create(ReserveApi::class.java)

    }

    suspend fun createReservation(
        stadiumId: Int,
        date: String,
        time: String,
        userName: String,
        userPhone: String
    ) {
        // 예약 정보를 생성
        val reservationRequest = ReservationRequest(
            stadiumId = stadiumId,
            date = date,
            time = time,
            userName = userName,
            userPhone = userPhone
        )

        // API 호출
        try {
            reserveApi.createReservation(reservationRequest)
            // 성공적으로 예약이 생성된 경우의 처리
        } catch (e: Exception) {
            // 네트워크 호출 중 예외 발생 시 처리
            e.printStackTrace()
            // 실패한 경우의 처리
        }
    }

    suspend fun createMatching(
        stadiumId: Int,
        comment: String,
        time: String,
        date: String,
        userName: String,
        contactPhone: String
    ){
        // 예약 정보를 생성
        val matchingRequest = MatchingRequest(
            stadiumId = stadiumId,
            comment = comment,
            date = date,
            time = time,
            userName = userName,
            contactPhone = contactPhone
        )

        // API 호출
        try {
            reserveApi.createMatching(matchingRequest)
            // 성공적으로 예약이 생성된 경우의 처리
        } catch (e: Exception) {
            // 네트워크 호출 중 예외 발생 시 처리
            e.printStackTrace()
            // 실패한 경우의 처리
        }
    }
}

interface ReserveApi{
    @GET("/stadium/reservation")
    suspend fun getReserveInfo1(@Query("stadiumId") stadiumId: Int, @Query("date") date: String): ReserveResponse

    @POST("/stadium/reservation")
    suspend fun createReservation(@Body request: ReservationRequest)

    @POST("/stadium/matching")
    suspend fun createMatching(@Body request: MatchingRequest)

    @GET("/stadium/matching")
    suspend fun successMatching(@Query("stadiumId") stadiumId: Int, @Query("date") date: String, @Query("time") time: String): MatchingConfirmRequest

    @GET("stadium/reservation/user")
    suspend fun getReserveInfo2(@Query("stadiumId") stadiumId: Int, @Query("date") date: String, @Query("time") time: String): ReservationRequest2
}



