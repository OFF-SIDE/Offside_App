package com.example.off_side_app.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class GroundInfo(
    @SerializedName(value = "id")
    val stadiumId: Int?,
    val location: String?,
    val contactPhone: String?,
    val name: String?,
    val address: String?,
    val comment: String?,
    val price: Int?,
    val image: String?
)

data class GroundInfoForPost(
    var location: String?,
    var name: String?,
    var contactPhone: String?,
    var address: String?,
    var comment: String?,
    var price: Int?,
    var image: String?
)

data class ImageUrl(
    val fileUrl: String
)

data class GroundInfoWithAvailableTime(
    val stadiumId: Int?,
    val location: String?,
    val contactPhone: String?,
    val name: String?,
    val address: String?,
    val comment: String?,
    val price: Int?,
    val image: String?,
    val availableTime: List<String>
)

data class GroundInfoGroup(
    val location : String,
    val groupedGround : MutableList<GroundInfo>
)

data class ReservedGroundInfo(
    val groundInfo: GroundInfo,
    val date: String,
    val time: String,
    val userName: String,
    val userPhone: String
)

data class ReservedGroundInfoGroup(
    val location,
    val groupedReservedGround : MutableList<ReservedGroundInfo>
)