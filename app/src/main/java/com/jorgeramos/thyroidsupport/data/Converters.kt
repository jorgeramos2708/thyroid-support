package com.jorgeramos.thyroidsupport.data

import androidx.room.TypeConverter
import com.jorgeramos.thyroidsupport.domain.ExerciseType
import com.jorgeramos.thyroidsupport.domain.MealType

class Converters {
    @TypeConverter
    fun fromListString(value: List<String>?): String {
        return value?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toListString(value: String?): List<String> {
        return if (value.isNullOrEmpty()) emptyList() else value.split(",")
    }

    @TypeConverter
    fun fromMealType(value: MealType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toMealType(value: String?): MealType? {
        return value?.let { enumValueOf<MealType>(it) }
    }

    @TypeConverter
    fun fromExerciseType(value: ExerciseType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toExerciseType(value: String?): ExerciseType? {
        return value?.let { enumValueOf<ExerciseType>(it) }
    }
}
