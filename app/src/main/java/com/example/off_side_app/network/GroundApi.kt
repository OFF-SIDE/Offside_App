package com.example.off_side_app.network

import com.example.off_side_app.GroundInfo
import com.example.off_side_app.GroundInfoForPost
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GroundApi {
    //@GET("googlecodelabs/kotlin-coroutines/master/advanced-coroutines-codelab/sunflower/src/main/assets/plants.json")
    @GET("stadium")
    suspend fun getGroundInfo(@Query("contactPhone") contactPhone: String = "", @Query("location") location: String = "마포구"): List<GroundInfo>
}

interface AddGroundApi {
    //@GET("googlecodelabs/kotlin-coroutines/master/advanced-coroutines-codelab/sunflower/src/main/assets/plants.json")
    @POST("stadium")
    suspend fun postGroundInfo(@Body groundInfoForPost: GroundInfoForPost): GroundInfo
}