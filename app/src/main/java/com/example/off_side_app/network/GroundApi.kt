package com.example.off_side_app.network

import com.example.off_side_app.GroundInfo
import com.example.off_side_app.GroundInfoForPost
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface GroundApi {
    @GET("stadium")
    suspend fun getGroundInfo(@Query("contactPhone") contactPhone: String = "", @Query("location") location: String = "마포구"): List<GroundInfo>
}

interface AddGroundApi {
    @POST("stadium")
    suspend fun postGroundInfo(@Body groundInfoForPost: GroundInfoForPost): GroundInfo
}

interface UploadImageApi{
    @Multipart
    @POST("image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ) : String
}