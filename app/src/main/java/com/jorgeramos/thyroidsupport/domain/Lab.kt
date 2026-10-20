package com.jorgeramos.thyroidsupport.domain

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lab",
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("patientId")]
)
data class Lab(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientId: Long,
    val testDate: Long, // epoch ms
    val testType: String, // e.g., "TSH", "FT4", "FT3", "TPO"
    val value: Double,
    val referenceLow: Double,
    val referenceHigh: Double
)
