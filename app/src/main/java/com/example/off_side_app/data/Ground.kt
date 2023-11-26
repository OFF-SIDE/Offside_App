package com.example.off_side_app.data

import android.net.Uri


data class Header(val locationPosition: Int)
data class Ground (
    var name: String?,
    var address: String?,
    var imagePath: Uri?,
    var locationPosition: Int,
    var reservations: ArrayList<Reservation> = ArrayList(),
)

data class Reservation(
    var startTime: Int, // 시작 시간 (24시간 형식, 예: 10, 11, ..., 19)
    var endTime: Int,   // 종료 시간 (24시간 형식, 예: 10, 11, ..., 19)
    var reserverName: String,
    var reserverPhoneNumber: String
)
