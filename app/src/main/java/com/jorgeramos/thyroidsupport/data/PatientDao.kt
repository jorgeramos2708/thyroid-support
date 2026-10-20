package com.jorgeramos.thyroidsupport.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jorgeramos.thyroidsupport.domain.Patient

@Dao
interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(patient: Patient): Long

    @Query("SELECT * FROM patient WHERE id = :id")
    suspend fun getPatient(id: Long): Patient?

    @Query("SELECT * FROM patient LIMIT 1")
    suspend fun getCurrentPatient(): Patient?

}
