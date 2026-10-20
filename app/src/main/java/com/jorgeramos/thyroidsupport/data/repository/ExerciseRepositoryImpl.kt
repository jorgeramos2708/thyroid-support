package com.jorgeramos.thyroidsupport.data.repository

import com.jorgeramos.thyroidsupport.data.ExerciseDao
import com.jorgeramos.thyroidsupport.domain.ExerciseEntry
import com.jorgeramos.thyroidsupport.domain.repository.ExerciseRepository
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseDao: ExerciseDao
) : ExerciseRepository {

    override suspend fun insert(entry: ExerciseEntry): Long =
        exerciseDao.insert(entry)

    override suspend fun getEntriesForDateRange(startDate: Long, endDate: Long): List<ExerciseEntry> =
        exerciseDao.getEntriesForDateRange(startDate, endDate)

    override suspend fun getEntriesForDate(date: Long): List<ExerciseEntry> =
        exerciseDao.getEntriesForDate(date)

    override suspend fun delete(id: Long) =
        exerciseDao.delete(id)
}
