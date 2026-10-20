package com.jorgeramos.thyroidsupport.data.repository

import com.jorgeramos.thyroidsupport.data.LabDao
import com.jorgeramos.thyroidsupport.domain.Lab
import com.jorgeramos.thyroidsupport.domain.repository.LabRepository
import javax.inject.Inject

class LabRepositoryImpl @Inject constructor(
    private val labDao: LabDao
) : LabRepository {

    override suspend fun insert(lab: Lab): Long =
        labDao.insert(lab)

    override suspend fun getLabsForPatient(patientId: Long): List<Lab> =
        labDao.getLabsForPatient(patientId)

    override suspend fun delete(id: Long) =
        labDao.delete(id)
}
