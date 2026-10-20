package com.jorgeramos.thyroidsupport.data.repository

import com.jorgeramos.thyroidsupport.data.DiaryDao
import com.jorgeramos.thyroidsupport.domain.DiaryEntry
import com.jorgeramos.thyroidsupport.domain.repository.DiaryRepository
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val diaryDao: DiaryDao
) : DiaryRepository {

    override suspend fun insert(entry: DiaryEntry): Long =
        diaryDao.insert(entry)

    override suspend fun getEntriesForDateRange(startDate: Long, endDate: Long): List<DiaryEntry> =
        diaryDao.getEntriesForDateRange(startDate, endDate)

    override suspend fun getEntriesForDate(date: Long): List<DiaryEntry> =
        diaryDao.getEntriesForDate(date)

    override suspend fun delete(id: Long) =
        diaryDao.delete(id)
}
