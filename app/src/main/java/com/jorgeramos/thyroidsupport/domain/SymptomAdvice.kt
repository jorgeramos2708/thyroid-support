package com.jorgeramos.thyroidsupport.domain

data class SymptomAdvice(
    val symptomId: String,
    val symptomName: String,
    val title: String,
    val tips: List<String>,
    val severity: Int // 0-10
)

data class AdviceCard(
    val title: String,
    val tips: List<String>,
    val isWarning: Boolean = false
)

object SymptomAdviceData {
    val allAdvices: List<SymptomAdvice> = listOf(
        SymptomAdvice(
            symptomId = "fatigue",
            symptomName = "Cansancio",
            title = "Cansancio",
            tips = listOf(
                "Mantén horarios fijos para dormir y despertar.",
                "Alterna actividad suave con pausas cortas."
            ),
            severity = 6
        ),
        SymptomAdvice(
            symptomId = "coldSensitivity",
            symptomName = "Sensibilidad al frío",
            title = "Sensibilidad al frío",
            tips = listOf(
                "Usa ropa en capas y bebidas tibias.",
                "Abriga manos y pies, y evita cambios bruscos de temperatura."
            ),
            severity = 4
        ),
        SymptomAdvice(
            symptomId = "palpitations",
            symptomName = "Palpitaciones",
            title = "Palpitaciones",
            tips = listOf(
                "Evita cafeína y estimulantes.",
                "Descansa y practica técnicas de respiración profunda.",
                "Consulta a tu médico si las palpitaciones son frecuentes."
            ),
            severity = 7
        ),
        SymptomAdvice(
            symptomId = "lowMood",
            symptomName = "Ánimo bajo",
            title = "Ánimo bajo",
            tips = listOf(
                "Realiza actividad física ligera al aire libre.",
                "Mantén una rutina diaria estructurada.",
                "Considera hablar con un profesional de salud mental."
            ),
            severity = 3
        ),
        SymptomAdvice(
            symptomId = "sleepProblems",
            symptomName = "Problemas de sueño",
            title = "Problemas de sueño",
            tips = listOf(
                "Evita cafeína por la tarde y pantallas antes de dormir.",
                "Cena ligera y mantén la habitación fresca y oscura."
            ),
            severity = 5
        )
    )

    val warningAdvice = AdviceCard(
        title = "Consulta sin esperar",
        tips = listOf(
            "Si tienes palpitaciones intensas, dolor de pecho, dificultad para respirar o tragar, o confusión."
        ),
        isWarning = true
    )
}
