package com.jorgeramos.thyroidsupport.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jorgeramos.thyroidsupport.domain.DiaryEntry

@Dao
interface DiaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DiaryEntry): Long

    @Query("SELECT * FROM diary_entry WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    suspend fun getEntriesForDateRange(startDate: Long, endDate: Long): List<DiaryEntry>

    @Query("SELECT * FROM diary_entry WHERE date = :date ORDER BY id DESC")
    suspend fun getEntriesForDate(date: Long): List<DiaryEntry>

    @Query("DELETE FROM diary_entry WHERE id = :id")
    suspend fun delete(id: Long)
}
