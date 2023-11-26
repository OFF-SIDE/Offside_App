package com.example.off_side_app.data

import com.example.off_side_app.data.AppDataManager.groundItems

object AppDataManager {
    var reserve = mutableMapOf<String, Reserve>()
    private var groundItems = ArrayList<ListItem>()
    val nearLocations = listOf(
        "마포구", "용산구", "서대문구", "은평구", "양천구",
        "강서구", "광진구", "성동구", "서초구", "영등포구"
    )

    fun getOriginalGroundItems(): ArrayList<ListItem> {
        return groundItems
    }

    fun isLocationExist(location:Int): Boolean{
        for (groundItem in groundItems) {
            if(groundItem.locationPosition == location){
                return true
            }
        }
        return false
    }
}