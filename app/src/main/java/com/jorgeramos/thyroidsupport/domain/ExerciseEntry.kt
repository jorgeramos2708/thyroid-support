package com.jorgeramos.thyroidsupport.domain

data class ExerciseEntry(
    val id: Long = 0,
    val date: Long, // epoch ms
    val type: ExerciseType,
    val durationMinutes: Int,
    val intensity: Int, // 1-10
    val notes: String?
)

enum class ExerciseType {
    CAMINATA, YOGA, PESAS_LIGERAS, ESTIRAMIENTOS, CICLISMO, NATACION, OTRO
}
