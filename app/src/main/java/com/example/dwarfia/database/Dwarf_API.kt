package com.example.dwarfia.database

import com.google.firebase.database.Exclude

data class Dwarf2(
    var name: String?= null,
    var longitude: String?= null,
    var latitude: String?= null,
    var address: String?= null,
    var description: String?=null,
    var img_link: String?= null,
    var star: Map<String, String>?= null,
    var heart: Map<String, String>?= null,
    var thumb_up: Map<String, String>?= null,
    var visited: Map<String, String>?= null
) {
    @Exclude
    fun getMap(): Map<String, Any?> {
        return mapOf("visited" to visited)
    }
}
