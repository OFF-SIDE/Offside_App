package com.example.off_side_app.data

object AppDataManager {
    private var groundItems = ArrayList<Ground>()
    val nearLocations = listOf(
        "마포구", "용산구", "서대문구", "은평구", "양천구",
        "강서구", "광진구", "성동구", "서초구", "영등포구"
    )

    fun getOriginalGroundItems(): ArrayList<Ground> {
        return groundItems
    }



}