package com.jorgeramos.thyroidsupport.data.repository

import com.jorgeramos.thyroidsupport.data.SupplementDao
import com.jorgeramos.thyroidsupport.domain.Supplement
import com.jorgeramos.thyroidsupport.domain.repository.SupplementRepository
import javax.inject.Inject

class SupplementRepositoryImpl @Inject constructor(
    private val supplementDao: SupplementDao
) : SupplementRepository {

    override suspend fun insert(supplement: Supplement): Long =
        supplementDao.insert(supplement)

    override suspend fun getSupplementsForPatient(patientId: Long): List<Supplement> =
        supplementDao.getSupplementsForPatient(patientId)

    override suspend fun delete(id: Long) =
        supplementDao.delete(id)
}
