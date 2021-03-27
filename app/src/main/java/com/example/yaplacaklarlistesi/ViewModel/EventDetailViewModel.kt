package com.example.yaplacaklarlistesi.ViewModel

import androidx.lifecycle.*
import com.example.yaplacaklarlistesi.Model.Event
import com.example.yaplacaklarlistesi.Room.Dao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(val dao: Dao) : ViewModel() {

    private var _event: LiveData<Event>? = null
    val event: LiveData<Event>?
        get() = _event

    fun getEvent(uid: Int) {
        _event = dao.getEventFromId(uid).asLiveData()
    }

    fun makeDate(year: Int, month: Int, day: Int) : String {
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("tr"))

        return LocalDate.of(year, month, day).format(dateFormatter)
    }

    fun completeEvent(complete: Boolean) {
        event?.let {
            it.value?.let {
                it.completed = complete
                viewModelScope.launch {
                    dao.updateEvent(it)
                }
            }
        }
    }

    fun makeTime(hour: Int?, minute: Int?) : String? {
        return if(hour == null || minute == null) {
            null
        }else {
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            LocalTime.of(hour, minute).format(timeFormatter)
        }
    }

    fun deleteEvent(uid: Int) {
        viewModelScope.launch {
            dao.deleteEvent(uid)
        }
    }

}