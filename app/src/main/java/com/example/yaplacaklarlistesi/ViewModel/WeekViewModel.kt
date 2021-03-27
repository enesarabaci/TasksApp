package com.example.yaplacaklarlistesi.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaplacaklarlistesi.Model.Date
import com.example.yaplacaklarlistesi.Room.Dao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class WeekViewModel @Inject constructor(val dao: Dao) : ViewModel() {

    private val _list = MutableLiveData<ArrayList<Date>>()
    val list: LiveData<ArrayList<Date>>
        get() = _list
    private var selectedDate = LocalDate.now()

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    private var dayOfMonth: Int? = null
    private var dayOfWeek: Int? = null

    fun previousWeek() {
        selectedDate = selectedDate.minusWeeks(1)
        updateData()
    }

    fun nextWeek() {
        selectedDate = selectedDate.plusWeeks(1)
        updateData()
    }

    fun updateData() {
        _list.value = makeWeek(selectedDate)
        makeTitle()
    }

    private fun makeTitle() {
        val formatter = DateTimeFormatter.ofPattern("dd/MMM", Locale("tr"))
        _list.value?.let {
            val firstDate = LocalDate.of(it.first().year, it.first().month, it.first().day)
            val lastDate = LocalDate.of(it.last().year, it.last().month, it.last().day)
            _title.value = "${firstDate.format(formatter)} - ${lastDate.format(formatter)}"
        }
    }

    private fun makeWeek(date: LocalDate) : ArrayList<Date> {
        lateinit var newDate: LocalDate
        val dates = ArrayList<Date>()

        dayOfMonth = date.dayOfMonth
        dayOfWeek = date.dayOfWeek.value

        var year = 0
        var month = 0
        var day = 0

        for (i in (dayOfMonth!!-(dayOfWeek!!-1))..(dayOfMonth!!+(7-dayOfWeek!!))) {

            if (i <= 0) {
                newDate = date.minusMonths(1)

                year = newDate.year
                month = newDate.monthValue
                day = newDate.lengthOfMonth()+i

                checkDatabase(year, month, day, dates.size)
            }
            else if(i>date.lengthOfMonth()) {
                newDate = date.plusMonths(1)

                year = newDate.year
                month = newDate.monthValue
                day = i-date.lengthOfMonth()

                checkDatabase(year, month, day, dates.size)
            }
            else {

                year = date.year
                month = date.monthValue
                day = i

                checkDatabase(year, month, day, dates.size)
            }
            dates.add(Date(year, month, day, times = null))
        }
        return dates
    }

    private fun checkDatabase(year: Int, month: Int, day: Int, position: Int) {
        viewModelScope.launch {

            val data = dao.getEvent(year, month, day)
            if (!data.isNullOrEmpty()) {
                for (d in data) {
                    if (d.hour != null && !d.completed) {
                        _list.value?.let {
                            val test = it
                            test.get(position).times?.add(d.hour!!) ?: kotlin.run {
                                test.get(position).times = arrayListOf(d.hour!!)
                            }
                            _list.value = test
                        }
                    }
                }
            }
        }
    }
}