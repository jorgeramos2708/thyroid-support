package com.jorgeramos.thyroidsupport.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_entry")
data class DiaryEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long, // epoch ms
    val mood: Int, // 1-10
    val energyLevel: Int, // 1-10
    val symptoms: List<String>,
    val notes: String?,
    val medicationTaken: Boolean,
    val labsDone: Boolean
)
