package com.jorgeramos.thyroidsupport.data.repository

import com.jorgeramos.thyroidsupport.data.PatientDao
import com.jorgeramos.thyroidsupport.domain.Patient
import com.jorgeramos.thyroidsupport.domain.repository.PatientRepository
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    private val patientDao: PatientDao
) : PatientRepository {

    override suspend fun insert(patient: Patient): Long =
        patientDao.insert(patient)

    override suspend fun getPatient(id: Long): Patient? =
        patientDao.getPatient(id)

    override suspend fun getCurrentPatient(): Patient? =
        patientDao.getCurrentPatient()
}
