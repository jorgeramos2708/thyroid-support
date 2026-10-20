package com.jorgeramos.thyroidsupport.domain.repository

import com.jorgeramos.thyroidsupport.domain.MealPlanEntry

interface MealPlanRepository {

    suspend fun insert(entry: MealPlanEntry): Long
    suspend fun getEntriesForDateRange(startDate: Long, endDate: Long): List<MealPlanEntry>
    suspend fun getEntriesForDate(date: Long): List<MealPlanEntry>
    suspend fun delete(id: Long)
}
