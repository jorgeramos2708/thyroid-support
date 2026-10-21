package com.jorgeramos.thyroidsupport.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jorgeramos.thyroidsupport.domain.LabTrendEntry

@Dao
interface LabTrendDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: LabTrendEntry): Long

    @Query("SELECT * FROM lab_trend_entry ORDER BY testDate DESC")
    suspend fun getAllTrends(): List<LabTrendEntry>

    @Query("SELECT * FROM lab_trend_entry WHERE testType = :testType ORDER BY testDate DESC")
    suspend fun getTrendsByType(testType: String): List<LabTrendEntry>

    @Query("DELETE FROM lab_trend_entry WHERE id = :id")
    suspend fun delete(id: Long)
}
