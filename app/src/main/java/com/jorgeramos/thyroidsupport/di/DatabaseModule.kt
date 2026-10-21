package com.jorgeramos.thyroidsupport.di

import android.content.Context
import com.jorgeramos.thyroidsupport.data.AppDatabase
import com.jorgeramos.thyroidsupport.data.DailySymptomDao
import com.jorgeramos.thyroidsupport.data.DiaryDao
import com.jorgeramos.thyroidsupport.data.ExerciseDao
import com.jorgeramos.thyroidsupport.data.LabDao
import com.jorgeramos.thyroidsupport.data.MealPlanDao
import com.jorgeramos.thyroidsupport.data.MedicationDao
import com.jorgeramos.thyroidsupport.data.PatientDao
import com.jorgeramos.thyroidsupport.data.SupplementDao
import com.jorgeramos.thyroidsupport.data.SymptomDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun providePatientDao(db: AppDatabase): PatientDao = db.patientDao()

    @Provides
    @Singleton
    fun provideSymptomDao(db: AppDatabase): SymptomDao = db.symptomDao()

    @Provides
    @Singleton
    fun provideLabDao(db: AppDatabase): LabDao = db.labDao()

    @Provides
    @Singleton
    fun provideMedicationDao(db: AppDatabase): MedicationDao = db.medicationDao()

    @Provides
    @Singleton
    fun provideSupplementDao(db: AppDatabase): SupplementDao = db.supplementDao()

    @Provides
    @Singleton
    fun provideMealPlanDao(db: AppDatabase): MealPlanDao = db.mealPlanDao()

    @Provides
    @Singleton
    fun provideExerciseDao(db: AppDatabase): ExerciseDao = db.exerciseDao()

    @Provides
    @Singleton
    fun provideDiaryDao(db: AppDatabase): DiaryDao = db.diaryDao()

    @Provides
    @Singleton
    fun provideDailySymptomDao(db: AppDatabase): DailySymptomDao = db.dailySymptomDao()
}
