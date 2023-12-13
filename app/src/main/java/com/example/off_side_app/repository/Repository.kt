package com.example.off_side_app.repository

import com.example.off_side_app.data.GroundInfo
import com.example.off_side_app.data.GroundInfoForPost
import com.example.off_side_app.data.GroundInfoWithAvailableTime
import com.example.off_side_app.data.ImageUrl
import com.example.off_side_app.data.NotificationInfo
import com.example.off_side_app.data.RefereeDetailInfo
import com.example.off_side_app.data.RefereeInfo
import com.example.off_side_app.data.RefereeInfoForPost
import com.example.off_side_app.data.ReservedGroundInfo
import com.example.off_side_app.network.AddGroundApi
import com.example.off_side_app.network.GetGroundDetailApi
import com.example.off_side_app.network.GetNotificationApi
import com.example.off_side_app.network.GetRefereeApi
import com.example.off_side_app.network.GetRefereeDetailApi
import com.example.off_side_app.network.GetReservedGroundApi
import com.example.off_side_app.network.GroundApi
import com.example.off_side_app.network.PostRefereeApi
import com.example.off_side_app.network.RetrofitInstance
import com.example.off_side_app.network.UploadImageApi
import okhttp3.MultipartBody

class Repository {
    private val getClient = RetrofitInstance.getInstance().create(GroundApi::class.java)
    private val postClient = RetrofitInstance.getInstance().create(AddGroundApi::class.java)
    private val imageClient = RetrofitInstance.getInstance().create(UploadImageApi::class.java)
    private val getDetailClient = RetrofitInstance.getInstance().create(GetGroundDetailApi::class.java)
    private val getReservedGroundClient = RetrofitInstance.getInstance().create(GetReservedGroundApi::class.java)
    private val getRefereeClient = RetrofitInstance.getInstance().create(GetRefereeApi::class.java)
    private val postRefereeClient = RetrofitInstance.getInstance().create(PostRefereeApi::class.java)
    private val getRefereeDetailClient = RetrofitInstance.getInstance().create(GetRefereeDetailApi::class.java)
    private val getNotificationClient = RetrofitInstance.getInstance().create(GetNotificationApi::class.java)

    suspend fun getGroundData(contactPhone: String, location: String) = getClient.getGroundInfo(contactPhone, location)
    suspend fun postGroundData(groundInfoForPost: GroundInfoForPost): GroundInfo {
        try{
            val ground = postClient.postGroundInfo(groundInfoForPost)
            return ground
        }
        catch (e:Exception){
            e.printStackTrace()
            return GroundInfo(1, "", "", "", "", "", 100, "", "")
        }
    }
    suspend fun uploadImageData(image: MultipartBody.Part): ImageUrl {
        try {
            val result = imageClient.uploadImage(image)
            return result
        }
        catch (e:Exception){
            e.printStackTrace()
            return ImageUrl("fail")
        }
    }

    suspend fun getGroundDetail(stadiumId: Int, date: Int): GroundInfoWithAvailableTime {
        try {
            val result = getDetailClient.getGroundDetail(stadiumId, date)
            return result
        }
        catch (e:Exception){
            e.printStackTrace()
            return GroundInfoWithAvailableTime(1, "", "", "", "", "", 100, "", listOf())
        }
    }

    suspend fun getReservedGroundData(userPhone: String): List<ReservedGroundInfo>{
        try {
            val result = getReservedGroundClient.getReservedGround(userPhone)
            return result
        }
        catch (e:Exception){
            e.printStackTrace()
            return listOf()
        }
    }

    suspend fun getRefereeData(date: String): List<RefereeInfo>{
        try {
            val result = getRefereeClient.getRefereeInfo(date)
            return result
        }
        catch (e:Exception){
            e.printStackTrace()
            return listOf()
        }
    }

    suspend fun postRefereeData(refereeInfoForPost: RefereeInfoForPost): RefereeInfo {
        try{
            val referee = postRefereeClient.postRefereeInfo(refereeInfoForPost)
            return referee
        }
        catch (e:Exception){
            e.printStackTrace()
            return RefereeInfo(1, "", "", "", "", 1, "")
        }
    }

    suspend fun getRefereeDetail(id: Int): RefereeDetailInfo{
        try{
            val result = getRefereeDetailClient.getRefereeDetail(id)
            return result
        }
        catch (e:Exception){
            e.printStackTrace()
            return RefereeDetailInfo(1, "", "", "", "", 1, "")
        }
    }

    suspend fun getNotificationData(contactPHone: String): List<NotificationInfo> {
        try{
            val result = getNotificationClient.getNotificationInfo(contactPHone)
            return result
        }
        catch (e:Exception){
            e.printStackTrace()
            return listOf()
        }
    }
}