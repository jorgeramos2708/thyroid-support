package com.jorgeramos.thyroidsupport.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jorgeramos.thyroidsupport.domain.ExerciseEntry

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: ExerciseEntry): Long

    @Query("SELECT * FROM exercise_entry WHERE date >= :startDate AND date <= :endDate ORDER BY date ASC")
    suspend fun getEntriesForDateRange(startDate: Long, endDate: Long): List<ExerciseEntry>

    @Query("SELECT * FROM exercise_entry WHERE date = :date ORDER BY id ASC")
    suspend fun getEntriesForDate(date: Long): List<ExerciseEntry>

    @Query("DELETE FROM exercise_entry WHERE id = :id")
    suspend fun delete(id: Long)
}
