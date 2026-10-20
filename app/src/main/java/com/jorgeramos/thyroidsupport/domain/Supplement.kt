package com.jorgeramos.thyroidsupport.domain

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "supplement",
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
data class Supplement(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientId: Long,
    val name: String,
    val dose: String,
    val startDate: Long,
    val endDate: Long? = null
)
