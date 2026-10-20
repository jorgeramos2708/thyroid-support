package com.jorgeramos.thyroidsupport.domain.repository

import com.jorgeramos.thyroidsupport.domain.DiaryEntry

interface DiaryRepository {

    suspend fun insert(entry: DiaryEntry): Long
    suspend fun getEntriesForDateRange(startDate: Long, endDate: Long): List<DiaryEntry>
    suspend fun getEntriesForDate(date: Long): List<DiaryEntry>
    suspend fun delete(id: Long)
}
