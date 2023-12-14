package com.example.off_side_app.network


class ReserveResponse(
    val matchingQ: List<String>,
    val reservationList: List<String>
)

data class ReservationRequest(
    val stadiumId: Int,
    val date: String,
    val time: String,
    val userName: String,
    val userPhone: String
)

data class MatchingRequest(
    val stadiumId: Int,
    val comment: String,
    val time: String,
    val date: String,
    val userName: String,
    val contactPhone: String
)

data class MatchingConfirmRequest(
    val id: Int,
    val stadiumId: Int,
    val comment: String,
    val time: String,
    val date: String,
    val userName: String,
    val contactPhone: String
)

data class ReservationRequest2(
    val id: Int,
    val stadiumId: Int,
    val date: String,
    val time: String,
    val userName: String,
    val userPhone: String,
)

data class RefereeRequest(
    val refereeId: Int,
    val date :String,
    val time: String,
    val userPhone: String,
    val userName: String,
    val comment: String
)

data class RefereeRequest2(
    val refereeId: Int,
    val date: String,
    val availableTime: List<String>
)

