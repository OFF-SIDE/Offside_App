package com.example.off_side_app.network

import com.example.off_side_app.data.GroundInfo
import com.example.off_side_app.data.GroundInfoForPost
import com.example.off_side_app.data.GroundInfoWithAvailableTime
import com.example.off_side_app.data.ImageUrl
import com.example.off_side_app.data.RefereeDetailInfo
import com.example.off_side_app.data.RefereeInfo
import com.example.off_side_app.data.RefereeInfoForPost
import com.example.off_side_app.data.ReservedGroundInfo
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
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
    ) : ImageUrl
}

interface GetGroundDetailApi{
    @GET("stadium/{stadiumId}")
    suspend fun getGroundDetail(
        @Path("stadiumId") stadiumId: Int,
        @Query("date") date: Int
    ) : GroundInfoWithAvailableTime
}


interface GetReservedGroundApi {
    @GET("stadium")
    suspend fun getReservedGround(@Query("userPhone") contactPhone: String = ""): List<ReservedGroundInfo>
}

// data가 있는게 부자연스럽다
interface GetRefereeApi{
    @GET("referee")
    suspend fun getRefereeInfo(@Query("location") location: String = "", @Query("date") date: String = ""): List<RefereeInfo>
}

interface PostRefereeApi{
    @POST("referee")
    suspend fun postRefereeInfo(@Body refereeInfoForPost: RefereeInfoForPost): RefereeInfo
}

interface GetRefereeDetailApi{
    @GET("referee/{id}")
    suspend fun getRefereeDetail(
        @Path("id") stadiumId: Int
    ) : RefereeDetailInfo
}