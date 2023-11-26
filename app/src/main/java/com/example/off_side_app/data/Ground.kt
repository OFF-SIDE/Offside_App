package com.example.off_side_app.data

import android.net.Uri

interface ListItem{
    companion object{
        val TYPE_HEADER = 1
        val TYPE_GROUND = 2
    }
    var locationPosition: Int
    fun getType():Int
}
data class Header(override var locationPosition: Int): ListItem {
    override fun getType():Int {
        return ListItem.TYPE_HEADER
    }
}
data class Ground (
    var name: String?,
    var address: String?,
    var imagePath: Uri?,
    override var locationPosition: Int,): ListItem {
    override fun getType():Int {
        return ListItem.TYPE_GROUND
    }
}

data class Reserve(
    val nameList: ArrayList<String>,
    val pnumList: ArrayList<String>,
    val day: BooleanArray = BooleanArray(24){false}
)



