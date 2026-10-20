package com.jorgeramos.thyroidsupport.data.repository

import com.jorgeramos.thyroidsupport.data.SymptomDao
import com.jorgeramos.thyroidsupport.domain.Symptom
import com.jorgeramos.thyroidsupport.domain.repository.SymptomRepository
import javax.inject.Inject

class SymptomRepositoryImpl @Inject constructor(
    private val symptomDao: SymptomDao
) : SymptomRepository {

    override suspend fun insert(symptom: Symptom): Long =
        symptomDao.insert(symptom)

    override suspend fun getSymptomsForPatient(patientId: Long): List<Symptom> =
        symptomDao.getSymptomsForPatient(patientId)

    override suspend fun delete(id: Long) =
        symptomDao.delete(id)
}
