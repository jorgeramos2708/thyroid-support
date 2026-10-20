package com.jorgeramos.thyroidsupport.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jorgeramos.thyroidsupport.domain.Symptom
import java.util.*

@Dao
interface SymptomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(symptom: Symptom): Long

    @Query("SELECT * FROM symptom WHERE patientId = :patientId ORDER BY recordedAt DESC")
    suspend fun getSymptomsForPatient(patientId: Long): List<Symptom>

    @Query("DELETE FROM symptom WHERE id = :id")
    suspend fun delete(id: Long)
}
