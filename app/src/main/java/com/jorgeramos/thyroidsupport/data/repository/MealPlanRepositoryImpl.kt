package com.jorgeramos.thyroidsupport.data.repository

import com.jorgeramos.thyroidsupport.data.MealPlanDao
import com.jorgeramos.thyroidsupport.domain.MealPlanEntry
import com.jorgeramos.thyroidsupport.domain.repository.MealPlanRepository
import javax.inject.Inject

class MealPlanRepositoryImpl @Inject constructor(
    private val mealPlanDao: MealPlanDao
) : MealPlanRepository {

    override suspend fun insert(entry: MealPlanEntry): Long =
        mealPlanDao.insert(entry)

    override suspend fun getEntriesForDateRange(startDate: Long, endDate: Long): List<MealPlanEntry> =
        mealPlanDao.getEntriesForDateRange(startDate, endDate)

    override suspend fun getEntriesForDate(date: Long): List<MealPlanEntry> =
        mealPlanDao.getEntriesForDate(date)

    override suspend fun delete(id: Long) =
        mealPlanDao.delete(id)
}
