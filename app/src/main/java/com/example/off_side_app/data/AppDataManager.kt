package com.example.off_side_app.data

import com.example.off_side_app.Ground

object AppDataManager {
    private var groundItems = ArrayList<Ground>()

    fun getOriginalGroundItems(): ArrayList<Ground> {
        return groundItems
    }
}