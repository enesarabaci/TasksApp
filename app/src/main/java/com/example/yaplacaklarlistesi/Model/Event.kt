package com.example.yaplacaklarlistesi.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDateTime

@Entity(tableName = "Event")
class Event(
    var title: String,
    var description: String? = null,
    var year: Int,
    var month: Int,
    var day: Int,
    var hour: Int?,
    var minute: Int?,
    var type: Type,
    var completed: Boolean = false,
    var localDateTime: LocalDateTime,
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0
) : Serializable