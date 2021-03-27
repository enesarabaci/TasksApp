package com.example.yaplacaklarlistesi.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Type")
class Type(
    var title: String,
    var color: String,
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0
) : Serializable