package com.example.off_side_app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroundInfo(
    @SerialName(value = "stadiumId")
    val stadiumId: Int?,
    val location: String?,
    @SerialName(value = "contactPhone")
    val contactPhone: String?,
    val name: String?,
    val address: String?,
    val comment: String?,
    val price: Int?,
    val image: String?
)
/*
@Serializable
data class GroundInfo(
    @SerialName(value = "stadiumId")
    val stadiumId: Int?,
    val location: String?,
    @SerialName(value = "contactPhone")
    val contactPhone: String?,
    val name: String?,
    val address: String?,
    val comment: String?,
    val price: Int?,
    val image: String?
)
*/