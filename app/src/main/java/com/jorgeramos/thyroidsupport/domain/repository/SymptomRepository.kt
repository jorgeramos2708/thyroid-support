package com.jorgeramos.thyroidsupport.domain.repository

import com.jorgeramos.thyroidsupport.domain.Symptom

interface SymptomRepository {

    suspend fun insert(symptom: Symptom): Long
    suspend fun getSymptomsForPatient(patientId: Long): List<Symptom>
    suspend fun delete(id: Long)
}
