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
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class MonthViewModel @Inject constructor(val dao: Dao) : ViewModel() {

    private val _list = MutableLiveData<ArrayList<Date?>>()
    val list: LiveData<ArrayList<Date?>>
        get() = _list
    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title
    private var selectedDate = LocalDate.now()

    fun previousMonth() {
        selectedDate = selectedDate.minusMonths(1)
        updateData()
    }
    fun nextMonth() {
        selectedDate = selectedDate.plusMonths(1)
        updateData()
    }

    fun updateData() {
        _title.value = makeMonth(selectedDate)
        _list.value = daysInMonthArray(selectedDate)
    }

    private fun makeMonth(date: LocalDate) : String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("tr"))
        return date.format(formatter)
    }

    private fun daysInMonthArray(date: LocalDate) : ArrayList<Date?> {
        val yearMonth: YearMonth = YearMonth.from(date)
        val daysInMonth: Int = yearMonth.lengthOfMonth()
        val firstOfMonth: LocalDate = date.withDayOfMonth(1)
        var dayOfWeek: Int = firstOfMonth.dayOfWeek.value
        dayOfWeek--

        val dates = ArrayList<Date?>()

        for (i in 1..43) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                dates.add(null)
            }else {
                val year = date.year
                val month = date.monthValue
                val day = i-dayOfWeek
                dates.add(Date(year, month, day, false))

                checkDatabase(year, month, day, i-1)
            }
        }
        return dates
    }

    private fun checkDatabase(year: Int, month: Int, day: Int, position: Int) {
        viewModelScope.launch {

            val data = dao.getEvent(year, month, day)
            if (!data.isNullOrEmpty()) {
                _list.value?.let {
                    val test = it
                    test.get(position)?.mark = true
                    var completed = true
                    for (d in data) {
                        if (!d.completed) {
                            completed = false
                            break
                        }
                    }
                    test.get(position)?.completed = completed

                    _list.value = test
                }
            }
        }
    }
}