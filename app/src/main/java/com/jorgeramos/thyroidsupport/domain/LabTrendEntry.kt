package com.jorgeramos.thyroidsupport.domain

data class LabTrendEntry(
    val id: Long = 0,
    val testType: String, // TSH, FT4, FT3, Anti-TPO
    val value: Double,
    val testDate: Long, // epoch ms
    val referenceLow: Double?,
    val referenceHigh: Double?,
    val unit: String? = null
)
