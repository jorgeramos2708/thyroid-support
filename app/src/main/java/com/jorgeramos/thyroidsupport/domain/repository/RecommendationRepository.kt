package com.jorgeramos.thyroidsupport.domain.repository

import com.jorgeramos.thyroidsupport.domain.LabResultAnalysis
import com.jorgeramos.thyroidsupport.domain.Patient
import com.jorgeramos.thyroidsupport.domain.Recommendation

interface RecommendationRepository {

    suspend fun generateRecommendation(
        patient: Patient,
        latestLabAnalysis: LabResultAnalysis
    ): Recommendation

    suspend fun analyzeLabResults(
        tsh: Double?,
        ft4: Double?,
        ft3: Double?,
        tpoAb: Double?,
        tshRefLow: Double?,
        tshRefHigh: Double?,
        ft4RefLow: Double?,
        ft4RefHigh: Double?,
        ft3RefLow: Double?,
        ft3RefHigh: Double?,
        tpoAbRefHigh: Double?
    ): LabResultAnalysis
}
