package com.jorgeramos.thyroidsupport.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jorgeramos.thyroidsupport.domain.Supplement

@Dao
interface SupplementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(supplement: Supplement): Long

    @Query("SELECT * FROM supplement WHERE patientId = :patientId ORDER BY startDate DESC")
    suspend fun getSupplementsForPatient(patientId: Long): List<Supplement>

    @Query("DELETE FROM supplement WHERE id = :id")
    suspend fun delete(id: Long)
}
