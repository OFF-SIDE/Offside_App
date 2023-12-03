package com.example.off_side_app.repository

import com.example.off_side_app.GroundInfoForPost
import com.example.off_side_app.network.AddGroundApi
import com.example.off_side_app.network.GroundApi
import com.example.off_side_app.network.RetrofitInstance
import com.example.off_side_app.network.RetrofitInstance.client

class Repository {
    private val getClient = RetrofitInstance.getInstance().create(GroundApi::class.java)
    private val postClient = RetrofitInstance.getInstance().create(AddGroundApi::class.java)

    suspend fun getGroundData(contactPhone: String, location: String) = getClient.getGroundInfo(contactPhone, location)
    suspend fun postGroundData(groundInfoForPost: GroundInfoForPost) = postClient.postGroundInfo(groundInfoForPost)
}