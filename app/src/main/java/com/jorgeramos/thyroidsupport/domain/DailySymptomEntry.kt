package com.jorgeramos.thyroidsupport.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_symptom_entry")
data class DailySymptomEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long, // epoch ms del día
    val fatigue: Int, // 0-10 Cansancio
    val coldSensitivity: Int, // 0-10 Sensibilidad al frío
    val palpitations: Int, // 0-10 Palpitaciones
    val lowMood: Int, // 0-10 Ánimo bajo
    val sleepProblems: Int, // 0-10 Problemas de sueño
    val weightKg: Double, // Peso en kg
    val heartRate: Int, // Pulso en lpm
    val doctorNote: String? = null // Nota para el médico (opcional)
)
