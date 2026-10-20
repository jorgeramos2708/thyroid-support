package com.jorgeramos.thyroidsupport.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jorgeramos.thyroidsupport.domain.Medication

@Dao
interface MedicationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medication: Medication): Long

    @Query("SELECT * FROM medication WHERE patientId = :patientId ORDER BY startDate DESC")
    suspend fun getMedicationsForPatient(patientId: Long): List<Medication>

    @Query("DELETE FROM medication WHERE id = :id")
    suspend fun delete(id: Long)
}
