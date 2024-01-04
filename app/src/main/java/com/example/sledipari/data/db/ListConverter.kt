package com.example.sledipari.data.db

import androidx.room.TypeConverter
import com.example.sledipari.jsonInstance
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class ListConverter {

    @TypeConverter
    fun fromList(list: List<String>): String {
        return jsonInstance.encodeToString(list)
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return jsonInstance.decodeFromString<List<String>>(value)
    }
}