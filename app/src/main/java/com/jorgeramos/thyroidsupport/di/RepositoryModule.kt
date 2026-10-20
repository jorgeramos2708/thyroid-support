package com.jorgeramos.thyroidsupport.di

import com.jorgeramos.thyroidsupport.data.repository.ExportRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.LabRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.MedicationRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.PatientRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.RecommendationRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.SupplementRepositoryImpl
import com.jorgeramos.thyroidsupport.data.repository.SymptomRepositoryImpl
import com.jorgeramos.thyroidsupport.domain.repository.ExportRepository
import com.jorgeramos.thyroidsupport.domain.repository.LabRepository
import com.jorgeramos.thyroidsupport.domain.repository.MedicationRepository
import com.jorgeramos.thyroidsupport.domain.repository.PatientRepository
import com.jorgeramos.thyroidsupport.domain.repository.RecommendationRepository
import com.jorgeramos.thyroidsupport.domain.repository.SupplementRepository
import com.jorgeramos.thyroidsupport.domain.repository.SymptomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}
