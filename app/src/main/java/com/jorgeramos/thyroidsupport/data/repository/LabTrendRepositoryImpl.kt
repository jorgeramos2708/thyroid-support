package com.jorgeramos.thyroidsupport.data.repository

import com.jorgeramos.thyroidsupport.data.LabTrendDao
import com.jorgeramos.thyroidsupport.domain.LabTrendEntry
import com.jorgeramos.thyroidsupport.domain.repository.LabTrendRepository
import javax.inject.Inject

class LabTrendRepositoryImpl @Inject constructor(
    private val labTrendDao: LabTrendDao
) : LabTrendRepository {

    override suspend fun insert(entry: LabTrendEntry): Long =
        labTrendDao.insert(entry)

    override suspend fun getAllTrends(): List<LabTrendEntry> =
        labTrendDao.getAllTrends()

    override suspend fun getTrendsByType(testType: String): List<LabTrendEntry> =
        labTrendDao.getTrendsByType(testType)

    override suspend fun delete(id: Long) =
        labTrendDao.delete(id)
}
