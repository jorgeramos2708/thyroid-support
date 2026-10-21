package com.jorgeramos.thyroidsupport.domain.repository

import com.jorgeramos.thyroidsupport.domain.DailySymptomEntry

interface DailySymptomRepository {

    suspend fun insert(entry: DailySymptomEntry): Long
    suspend fun getLatestEntry(): DailySymptomEntry?
    suspend fun getEntriesForDateRange(startDate: Long, endDate: Long): List<DailySymptomEntry>
    suspend fun delete(id: Long)
}
