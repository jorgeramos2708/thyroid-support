package com.jorgeramos.thyroidsupport.data.repository

import com.jorgeramos.thyroidsupport.data.DailySymptomDao
import com.jorgeramos.thyroidsupport.domain.DailySymptomEntry
import com.jorgeramos.thyroidsupport.domain.repository.DailySymptomRepository
import javax.inject.Inject

class DailySymptomRepositoryImpl @Inject constructor(
    private val dailySymptomDao: DailySymptomDao
) : DailySymptomRepository {

    override suspend fun insert(entry: DailySymptomEntry): Long =
        dailySymptomDao.insert(entry)

    override suspend fun getLatestEntry(): DailySymptomEntry? =
        dailySymptomDao.getLatestEntry()

    override suspend fun getEntriesForDateRange(startDate: Long, endDate: Long): List<DailySymptomEntry> =
        dailySymptomDao.getEntriesForDateRange(startDate, endDate)

    override suspend fun delete(id: Long) =
        dailySymptomDao.delete(id)
}
