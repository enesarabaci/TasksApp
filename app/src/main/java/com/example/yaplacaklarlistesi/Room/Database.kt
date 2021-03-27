package com.example.yaplacaklarlistesi.Room

import androidx.room.*
import androidx.room.Database
import com.example.yaplacaklarlistesi.Model.Event
import com.example.yaplacaklarlistesi.Model.Type

@Database(entities = arrayOf(Event::class, Type::class), version = 1)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun dao() : Dao

}