package com.example.yaplacaklarlistesi.Room

import androidx.room.*
import androidx.room.Dao
import com.example.yaplacaklarlistesi.Model.Event
import com.example.yaplacaklarlistesi.Model.Type
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface Dao {

    fun getEvent(query: String, hide: Boolean, year: Int? = null, month: Int? = null, day: Int? = null, hour: Int? = null) : Flow<List<Event>> {

        return if (hour != null) {
            getEventsTime(query, hide, year!!, month!!, day!!, hour)
        }else if (year != null) {
            getEventsDate(query, hide, year, month!!, day!!)
        }else {
            getEvents(query, hide)
        }


    }



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEvent(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveType(type: Type)



    @Query("SELECT * FROM Event WHERE (completed != :hide OR completed = 0) AND title LIKE '%' || :query || '%' ORDER BY localDateTime ASC")
    fun getEvents(query: String, hide: Boolean) : Flow<List<Event>>

    @Query("SELECT * FROM Event WHERE (completed != :hide OR completed = 0) AND title LIKE '%' || :query || '%' AND year = :year AND month = :month AND day = :day ORDER BY localDateTime ASC")
    fun getEventsDate(query: String, hide: Boolean, year: Int, month: Int, day: Int) : Flow<List<Event>>

    @Query("SELECT * FROM Event WHERE (completed != :hide OR completed = 0) AND title LIKE '%' || :query || '%' AND year = :year AND month = :month AND day = :day AND hour = :hour ORDER BY localDateTime ASC")
    fun getEventsTime(query: String, hide: Boolean, year: Int, month: Int, day: Int, hour: Int) : Flow<List<Event>>



    @Query("SELECT * FROM Event WHERE year = :year AND month = :month AND day = :day")
    suspend fun getEvent(year: Int, month: Int, day: Int) : List<Event>

    @Query("SELECT * FROM Event WHERE (completed != :hide OR completed = 0) AND title LIKE '%' || :query || '%' AND localDateTime < :time ORDER BY localDateTime ASC")
    fun getLateEvents(query: String, hide: Boolean, time: LocalDateTime) : Flow<List<Event>>

    @Query("SELECT * FROM Event WHERE uid = :id")
    fun getEventFromId(id: Int) : Flow<Event>

    @Query("SELECT * FROM Type")
    fun getTypesFlow() : Flow<List<Type>>

    @Query("SELECT * FROM Type")
    suspend fun getTypes() : List<Type>

    @Query("DELETE FROM Event WHERE uid = :id")
    suspend fun deleteEvent(id: Int)


    @Query("DELETE FROM Event WHERE completed = 1")
    suspend fun deleteAllCompleteEvents()

    @Query("DELETE FROM Event WHERE completed = 1 AND year = :year AND month = :month AND day = :day")
    suspend fun deleteAllCompleteEventsDate(year: Int, month: Int, day: Int)

    @Query("DELETE FROM Event WHERE completed = 1 AND year = :year AND month = :month AND day = :day AND hour = :hour")
    suspend fun deleteAllCompleteEventsTime(year: Int, month: Int, day: Int, hour: Int)


    @Query("DELETE FROM Event WHERE completed = 1 AND localDateTime < :time")
    suspend fun deleteAllCompleteLateEvents(time: LocalDateTime)

    @Delete
    suspend fun deleteType(type: Type)

    @Update
    suspend fun updateEvent(event: Event)

}