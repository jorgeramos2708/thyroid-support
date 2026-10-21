package com.jorgeramos.thyroidsupport.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jorgeramos.thyroidsupport.domain.DailySymptomEntry
import com.jorgeramos.thyroidsupport.domain.DiaryEntry
import com.jorgeramos.thyroidsupport.domain.ExerciseEntry
import com.jorgeramos.thyroidsupport.domain.Lab
import com.jorgeramos.thyroidsupport.domain.MealPlanEntry
import com.jorgeramos.thyroidsupport.domain.Medication
import com.jorgeramos.thyroidsupport.domain.Patient
import com.jorgeramos.thyroidsupport.domain.Supplement
import com.jorgeramos.thyroidsupport.domain.Symptom

@Database(
    entities = [
        Patient::class,
        Symptom::class,
        Lab::class,
        Medication::class,
        Supplement::class,
        MealPlanEntry::class,
        ExerciseEntry::class,
        DiaryEntry::class,
        DailySymptomEntry::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun patientDao(): PatientDao
    abstract fun symptomDao(): SymptomDao
    abstract fun labDao(): LabDao
    abstract fun medicationDao(): MedicationDao
    abstract fun supplementDao(): SupplementDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun diaryDao(): DiaryDao
    abstract fun dailySymptomDao(): DailySymptomDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "thyroid_support.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
