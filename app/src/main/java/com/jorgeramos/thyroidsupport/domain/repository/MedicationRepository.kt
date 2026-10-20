package com.jorgeramos.thyroidsupport.domain.repository

import com.jorgeramos.thyroidsupport.domain.Medication

interface MedicationRepository {

    suspend fun insert(medication: Medication): Long
    suspend fun getMedicationsForPatient(patientId: Long): List<Medication>
    suspend fun delete(id: Long)
}
