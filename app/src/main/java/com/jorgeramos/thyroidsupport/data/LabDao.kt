package com.jorgeramos.thyroidsupport.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jorgeramos.thyroidsupport.domain.Lab

@Dao
interface LabDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lab: Lab): Long

    @Query("SELECT * FROM lab WHERE patientId = :patientId ORDER BY testDate DESC")
    suspend fun getLabsForPatient(patientId: Long): List<Lab>

    @Query("DELETE FROM lab WHERE id = :id")
    suspend fun delete(id: Long)
}
