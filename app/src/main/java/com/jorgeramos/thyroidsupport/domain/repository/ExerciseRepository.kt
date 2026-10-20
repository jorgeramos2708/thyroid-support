package com.jorgeramos.thyroidsupport.domain.repository

import com.jorgeramos.thyroidsupport.domain.ExerciseEntry

interface ExerciseRepository {

    suspend fun insert(entry: ExerciseEntry): Long
    suspend fun getEntriesForDateRange(startDate: Long, endDate: Long): List<ExerciseEntry>
    suspend fun getEntriesForDate(date: Long): List<ExerciseEntry>
    suspend fun delete(id: Long)
}
