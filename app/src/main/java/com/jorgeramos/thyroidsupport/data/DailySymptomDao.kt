package com.jorgeramos.thyroidsupport.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jorgeramos.thyroidsupport.domain.DailySymptomEntry

@Dao
interface DailySymptomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DailySymptomEntry): Long

    @Query("SELECT * FROM daily_symptom_entry ORDER BY date DESC LIMIT 1")
    suspend fun getLatestEntry(): DailySymptomEntry?

    @Query("SELECT * FROM daily_symptom_entry WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    suspend fun getEntriesForDateRange(startDate: Long, endDate: Long): List<DailySymptomEntry>

    @Query("DELETE FROM daily_symptom_entry WHERE id = :id")
    suspend fun delete(id: Long)
}
