package com.jorgeramos.thyroidsupport.data.repository

import com.jorgeramos.thyroidsupport.data.MedicationDao
import com.jorgeramos.thyroidsupport.domain.Medication
import com.jorgeramos.thyroidsupport.domain.repository.MedicationRepository
import javax.inject.Inject

class MedicationRepositoryImpl @Inject constructor(
    private val medicationDao: MedicationDao
) : MedicationRepository {

    override suspend fun insert(medication: Medication): Long =
        medicationDao.insert(medication)

    override suspend fun getMedicationsForPatient(patientId: Long): List<Medication> =
        medicationDao.getMedicationsForPatient(patientId)

    override suspend fun delete(id: Long) =
        medicationDao.delete(id)
}
