package com.example.off_side_app.repository

import com.example.off_side_app.network.GroundApi
import com.example.off_side_app.network.RetrofitInstance

class Repository {
    private val client = RetrofitInstance.getInstance().create(GroundApi::class.java)

    suspend fun getAllData(contactPhone: String, location: String) = client.getAllPlant(contactPhone, location)
}