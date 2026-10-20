package com.jorgeramos.thyroidsupport.domain.repository

import com.jorgeramos.thyroidsupport.domain.Patient

interface PatientRepository {

    suspend fun insert(patient: Patient): Long
    suspend fun getPatient(id: Long): Patient?
    suspend fun getCurrentPatient(): Patient?
}
