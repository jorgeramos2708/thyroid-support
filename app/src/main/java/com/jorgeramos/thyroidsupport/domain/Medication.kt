package com.jorgeramos.thyroidsupport.domain

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "medication",
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
data class Medication(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientId: Long,
    val name: String,
    val dose: String,
    val frequency: String, // e.g., "OD", "BID", "TID"
    val startDate: Long, // epoch ms
    val endDate: Long? = null // epoch ms
)
