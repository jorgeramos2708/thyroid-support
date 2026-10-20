package com.jorgeramos.thyroidsupport.domain

data class MealPlanEntry(
    val id: Long = 0,
    val date: Long, // epoch ms
    val mealType: MealType, // DESAYUNO, ALMUERZO, CENA, MERIENDA
    val foods: List<String>,
    val calories: Int,
    val iodineMcg: Double?,
    val seleniumMcg: Double?,
    val notes: String?
)

enum class MealType {
    DESAYUNO, ALMUERZO, CENA, MERIENDA
}
