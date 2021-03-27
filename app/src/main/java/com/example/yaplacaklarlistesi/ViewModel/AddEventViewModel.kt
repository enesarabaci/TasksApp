package com.example.yaplacaklarlistesi.ViewModel

import androidx.lifecycle.*
import com.example.yaplacaklarlistesi.Model.Event
import com.example.yaplacaklarlistesi.Model.Type
import com.example.yaplacaklarlistesi.Room.Dao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class AddEventViewModel @Inject constructor(val dao: Dao) : ViewModel() {

    val types = dao.getTypesFlow().asLiveData()
    var event: Event? = null

    init {
        getTypes()
    }

    fun getTypes() {
        viewModelScope.launch {
            dao.getTypes().also {
                if(it.isNullOrEmpty()) {
                    loadTypes()
                }
            }
        }
    }

    fun saveData(
        title: String,
        description: String?,
        date: LocalDate,
        time: LocalTime?,
        type: Type
    ) {
        event?.let {
            it.title = title
            it.description = description
            it.year = date.year
            it.month = date.monthValue
            it.day = date.dayOfMonth
            it.hour = time?.hour
            it.minute = time?.minute
            it.type = type
            it.localDateTime = LocalDateTime.of(date.year, date.month, date.dayOfMonth, time?.hour ?: 23, time?.minute ?: 59)
            viewModelScope.launch {
                dao.updateEvent(it)
            }
        } ?: run {
            val event = Event(title,
                description,
                date.year,
                date.monthValue,
                date.dayOfMonth,
                time?.hour,
                time?.minute,
                type,
                localDateTime = LocalDateTime.of(date.year, date.month, date.dayOfMonth, time?.hour ?: 23, time?.minute ?: 59)
            )

            viewModelScope.launch {
                dao.saveEvent(event)
            }
        }

    }

    fun makeTypeList(list: List<Type>) : Array<CharSequence> {
        val newList = ArrayList<CharSequence>()
        for (type in list) {
            newList.add(type.title)
        }
        val array = newList.toTypedArray()
        return array
    }

    private fun loadTypes() {
        val importantType = Type("Önemli", "#eb4034")
        val taskType = Type("Görev", "#34eb49")
        val dontForgetType = Type("Unutma", "#34b1eb")
        viewModelScope.launch {
            dao.saveType(importantType)
            dao.saveType(taskType)
            dao.saveType(dontForgetType)
        }
    }
}