package com.example.off_side_app.data

data class RefereeForPost(
    val location: String,
    val contactPhone: String,
    val name: String,
    val comment: String,
    val price: Int,
    val image: String,
    val availableTime: List<String>
)

data class RefereeInfo(
    val id: Int,
    val location: String,
    val contactPhone: String,
    val name: String,
    val comment: String,
    val price: Int,
    val image: String
)

data class RefereeInfoGroup(
    val location: String,
    val groupedReferee: List<RefereeInfo>
)