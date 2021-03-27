package com.example.yaplacaklarlistesi.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.yaplacaklarlistesi.Model.Type
import com.example.yaplacaklarlistesi.Room.Dao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventTypesViewModel @Inject constructor(val dao: Dao) : ViewModel() {

    private var _list: LiveData<List<Type>>? = null
    val list: LiveData<List<Type>>?
        get() = _list

    fun getEventTypes() {
        _list = dao.getTypesFlow().asLiveData()
        getTypes()
    }

    fun deleteType(type: Type) {
        viewModelScope.launch {
            dao.deleteType(type)
        }
    }

    private fun getTypes() {
        viewModelScope.launch {
            dao.getTypes().also {
                if(it.isNullOrEmpty()) {
                    loadTypes()
                }
            }
        }
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