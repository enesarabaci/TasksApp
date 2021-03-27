package com.example.yaplacaklarlistesi.Model

import java.io.Serializable

class Date(
    val year: Int,
    val month: Int,
    val day: Int,
    var mark: Boolean = false,
    var completed: Boolean = false,
    var times: ArrayList<Int>? = null
) : Serializable