package com.jorgeramos.thyroidsupport.domain

data class MedicationLog(
    val id: Long = 0,
    val medicationId: Long,
    val date: Long, // epoch ms del día
    val taken: Boolean,
    val notes: String? = null
)
