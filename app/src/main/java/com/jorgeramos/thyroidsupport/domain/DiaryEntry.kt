package com.jorgeramos.thyroidsupport.domain

data class DiaryEntry(
    val id: Long = 0,
    val date: Long, // epoch ms
    val mood: Int, // 1-10
    val energyLevel: Int, // 1-10
    val symptoms: List<String>,
    val notes: String?,
    val medicationTaken: Boolean,
    val labsDone: Boolean
)
