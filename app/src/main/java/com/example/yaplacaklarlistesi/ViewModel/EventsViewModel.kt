package com.example.yaplacaklarlistesi.ViewModel

import androidx.lifecycle.*
import com.example.yaplacaklarlistesi.Model.Event
import com.example.yaplacaklarlistesi.Room.Dao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(val dao: Dao) : ViewModel() {

    private var _list: LiveData<List<Event>>? = null
    val list: LiveData<List<Event>>?
        get() = _list

    private var _lateEvents: LiveData<List<Event>>? = null
    val lateEvents: LiveData<List<Event>>?
        get() = _lateEvents

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    val searchQuery = MutableStateFlow("")
    val hideCompletes = MutableStateFlow(false)


    @ExperimentalCoroutinesApi
    fun getEvents(localDate: Long, hour: Int) {
        if (localDate == -1L) {

            _list = combine(searchQuery, hideCompletes) { query, hide ->
                Pair(query, hide)
            }.flatMapLatest { (query, hide) ->
                dao.getEvent(query, hide)
            }.asLiveData()

        }
        else {
            val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("tr"))
            val date = LocalDate.ofEpochDay(localDate)
            if (hour == -1) {

                _list = combine(searchQuery, hideCompletes) { query, hide ->
                    Pair(query, hide)
                }.flatMapLatest { (query, hide) ->
                    dao.getEvent(query, hide, date.year, date.monthValue, date.dayOfMonth)
                }.asLiveData()

                _title.value = date.format(dateFormatter)
            }
            else {

                _list = combine(searchQuery, hideCompletes) { query, hide ->
                    Pair(query, hide)
                }.flatMapLatest { (query, hide) ->
                    dao.getEvent(query, hide, date.year, date.monthValue, date.dayOfMonth, hour)
                }.asLiveData()

                _title.value = "${date.format(dateFormatter)} | $hour:00"
            }
        }
    }

    fun getLateEvents() {

        _lateEvents = combine(searchQuery, hideCompletes) { query, hide ->
            Pair(query, hide)
        }.flatMapLatest { (query, hide) ->
            dao.getLateEvents(query, hide, LocalDateTime.now())
        }.asLiveData()

    }

    fun completeEvent(position: Int, completed: Boolean) {
        val event = _list?.value?.get(position)
        event?.let {
            it.completed = completed
            viewModelScope.launch {
                dao.updateEvent(it)
            }
        }
    }

    fun completeLateEvent(position: Int, completed: Boolean) {
        val event = _lateEvents?.value?.get(position)
        event?.let {
            it.completed = completed
            viewModelScope.launch {
                dao.updateEvent(it)
            }
        }
    }

    fun deleteAllCompleteEvents(localDate: Long, hour: Int) {
        viewModelScope.launch {
            if (localDate == -1L) {
                dao.deleteAllCompleteEvents()
            }
            else {
                val date = LocalDate.ofEpochDay(localDate)
                if (hour == -1) {
                    dao.deleteAllCompleteEventsDate(date.year, date.monthValue, date.dayOfMonth)
                }else {
                    dao.deleteAllCompleteEventsTime(date.year, date.monthValue, date.dayOfMonth, hour)
                }

            }
        }
    }

    fun deleteAllCompleteLateEvents() {
        viewModelScope.launch {
            dao.deleteAllCompleteLateEvents(LocalDateTime.now())
        }
    }

}