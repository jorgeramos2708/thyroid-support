package com.jorgeramos.thyroidsupport.domain

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "symptom",
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
data class Symptom(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientId: Long,
    val recordedAt: Long, // epoch ms
    val type: String, // e.g., "fatigue", "weight_gain", "palpitations"
    val severity: Int, // 0-10
    val notes: String? = null
)
