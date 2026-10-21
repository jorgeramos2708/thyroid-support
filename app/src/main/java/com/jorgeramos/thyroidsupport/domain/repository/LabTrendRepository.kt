package com.jorgeramos.thyroidsupport.domain.repository

import com.jorgeramos.thyroidsupport.domain.LabTrendEntry

interface LabTrendRepository {

    suspend fun insert(entry: LabTrendEntry): Long
    suspend fun getAllTrends(): List<LabTrendEntry>
    suspend fun getTrendsByType(testType: String): List<LabTrendEntry>
    suspend fun delete(id: Long)
}
