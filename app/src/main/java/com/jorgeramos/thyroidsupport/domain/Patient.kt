package com.jorgeramos.thyroidsupport.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient")
data class Patient(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val birthDate: Long, // epoch ms
    val gender: String // "M" | "F" | "O"
)
