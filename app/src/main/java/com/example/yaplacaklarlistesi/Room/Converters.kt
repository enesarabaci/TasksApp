package com.example.yaplacaklarlistesi.Room

import androidx.room.TypeConverter
import com.example.yaplacaklarlistesi.Model.Type
import com.google.gson.Gson
import java.time.*

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun typeToString(type: Type) : String {
        val list = listOf<String>(type.title, type.color.toString())
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToList(string: String) : Type {
        val list = gson.fromJson(string, Array<String>::class.java).toList()
        return Type(list.get(0), list.get(1))
    }

    @TypeConverter
    fun localDateTimeToLong(localDateTime: LocalDateTime) : Long =
        localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond()

    @TypeConverter
    fun longToLocalDateTime(long: Long) : LocalDateTime =
        LocalDateTime.ofInstant(Instant.ofEpochSecond(long), ZoneId.systemDefault())



}