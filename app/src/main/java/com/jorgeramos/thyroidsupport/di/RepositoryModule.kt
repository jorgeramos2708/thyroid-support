package com.jorgeramos.thyroidsupport.di

import android.content.Context
import com.jorgeramos.thyroidsupport.data.repository.DailySymptomRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.DiaryRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.ExerciseRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.ExportRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.LabRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.MealPlanRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.MedicationLogRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.MedicationRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.OpenAIServiceImpl
import com.jorgeramos.thyroidsupport.data.repository.PatientRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.RecommendationRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.SupplementRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.SymptomRepositoryImpl
import com.jorgeramos.thyroidsupport.domain.repository.DailySymptomRepository
import com.jorgeramos.thyroidsupport.domain.repository.DiaryRepository
import com.jorgeramos.thyroidsupport.domain.repository.ExerciseRepository
import com.jorgeramos.thyroidsupport.domain.repository.ExportRepository
import com.jorgeramos.thyroidsupport.domain.repository.LabRepository
import com.jorgeramos.thyroidsupport.domain.repository.MealPlanRepository
import com.jorgeramos.thyroidsupport.domain.repository.MedicationLogRepository
import com.jorgeramos.thyroidsupport.domain.repository.MedicationRepository
import com.jorgeramos.thyroidsupport.domain.repository.OpenAIService
import com.jorgeramos.thyroidsupport.domain.repository.PatientRepository
import com.jorgeramos.thyroidsupport.domain.repository.RecommendationRepository
import com.jorgeramos.thyroidsupport.domain.repository.SupplementRepository
import com.jorgeramos.thyroidsupport.domain.repository.SymptomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providePatientRepository(impl: PatientRepositoryImpl): PatientRepository = impl

    @Provides
    fun provideSymptomRepository(impl: SymptomRepositoryImpl): SymptomRepository = impl

    @Provides
    fun provideLabRepository(impl: LabRepositoryImpl): LabRepository = impl

    @Provides
    fun provideMedicationRepository(impl: MedicationRepositoryImpl): MedicationRepository = impl

    @Provides
    fun provideSupplementRepository(impl: SupplementRepositoryImpl): SupplementRepository = impl

    @Provides
    fun provideRecommendationRepository(impl: RecommendationRepositoryImpl): RecommendationRepository = impl

    @Provides
    fun provideExportRepository(impl: ExportRepositoryImpl): ExportRepository = impl

    @Provides
    fun provideMealPlanRepository(impl: MealPlanRepositoryImpl): MealPlanRepository = impl

    @Provides
    fun provideExerciseRepository(impl: ExerciseRepositoryImpl): ExerciseRepository = impl

    @Provides
    fun provideDiaryRepository(impl: DiaryRepositoryImpl): DiaryRepository = impl

    @Provides
    fun provideOpenAIService(@ApplicationContext context: Context): OpenAIService = OpenAIServiceImpl(context)

    @Provides
    fun provideDailySymptomRepository(impl: DailySymptomRepositoryImpl): DailySymptomRepository = impl

    @Provides
    fun provideMedicationLogRepository(impl: MedicationLogRepositoryImpl): MedicationLogRepository = impl
}
