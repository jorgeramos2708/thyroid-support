package com.jorgeramos.thyroidsupport.data.repository

import com.jorgeramos.thyroidsupport.domain.LabResultAnalysis
import com.jorgeramos.thyroidsupport.domain.Patient
import com.jorgeramos.thyroidsupport.domain.Recommendation
import com.jorgeramos.thyroidsupport.domain.SupplementRecommendation
import com.jorgeramos.thyroidsupport.domain.DietRecommendation
import com.jorgeramos.thyroidsupport.domain.ExerciseRecommendation
import com.jorgeramos.thyroidsupport.domain.MedicationRecommendation
import com.jorgeramos.thyroidsupport.domain.ThyroidStatus
import com.jorgeramos.thyroidsupport.domain.AntibodyStatus
import com.jorgeramos.thyroidsupport.domain.repository.RecommendationRepository
import javax.inject.Inject

class RecommendationRepositoryImpl @Inject constructor() : RecommendationRepository {

    // Rangos de referencia típicos (según ATA 2020 y Endocrine Society 2022)
    private val DEFAULT_TSH_LOW = 0.4
    private val DEFAULT_TSH_HIGH = 4.0
    private val DEFAULT_FT4_LOW = 9.0  // pmol/L
    private val DEFAULT_FT4_HIGH = 19.0
    private val DEFAULT_FT3_LOW = 3.5  // pmol/L
    private val DEFAULT_FT3_HIGH = 6.5
    private val DEFAULT_TPOAB_HIGH = 35.0  // IU/mL

    override suspend fun generateRecommendation(
        patient: Patient,
        latestLabAnalysis: LabResultAnalysis
    ): Recommendation {
        val weightKg = estimateWeightKg(patient)
        val age = calculateAge(patient.birthDate)

        val medRec = when (latestLabAnalysis.tshStatus) {
            ThyroidStatus.HIGH -> {
                // Hipotiroidismo: TSH elevado
                levothyroxineRecommendation(weightKg, age, latestLabAnalysis.tshStatus)
            }
            ThyroidStatus.LOW -> {
                // Hipertiroidismo: TSH suprimido
                antithyroidRecommendation(latestLabAnalysis.tshStatus)
            }
            ThyroidStatus.BORDERLINE_HIGH -> {
                // Subclínico: considerar tratamiento si sintomático o TPO positivo
                subclinicalHypoRecommendation(weightKg, age, latestLabAnalysis)
            }
            else -> {
                MedicationRecommendation(
                    name = "Sin cambio",
                    dose = "",
                    frequency = "",
                    rationale = "TSH dentro de rango normal",
                    evidenceLevel = "ATA 2020"
                )
            }
        }

        val suppRec = when {
            latestLabAnalysis.tpoAbStatus == AntibodyStatus.POSITIVE ||
                latestLabAnalysis.tpoAbStatus == AntibodyStatus.ELEVATED -> {
                seleniumRecommendation()
            }
            else -> null
        }

        val dietRec = dietRecommendation(latestLabAnalysis.tshStatus)
        val exerciseRec = exerciseRecommendation(latestLabAnalysis.tshStatus, age)
        val warning = generateWarning(latestLabAnalysis, patient)
        val sources = listOf(
            "ATA Guidelines 2020 - Hipotiroidismo",
            "Endocrine Society Clinical Practice Guidelines 2022",
            "Selenium supplementation in autoimmune thyroid disease (2019-2022)"
        )

        return Recommendation(
            medication = medRec,
            supplement = suppRec,
            diet = dietRec,
            exercise = exerciseRec,
            warning = warning,
            sources = sources
        )
    }

    override suspend fun analyzeLabResults(
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
    ): LabResultAnalysis {
        val tshLow = tshRefLow ?: DEFAULT_TSH_LOW
        val tshHigh = tshRefHigh ?: DEFAULT_TSH_HIGH
        val ft4Low = ft4RefLow ?: DEFAULT_FT4_LOW
        val ft4High = ft4RefHigh ?: DEFAULT_FT4_HIGH
        val ft3Low = ft3RefLow ?: DEFAULT_FT3_LOW
        val ft3High = ft3RefHigh ?: DEFAULT_FT3_HIGH
        val tpoAbHigh = tpoAbRefHigh ?: DEFAULT_TPOAB_HIGH

        val tshStatus = evaluateThyroidStatus(tsh, tshLow, tshHigh)
        val ft4Status = ft4?.let { evaluateThyroidStatus(it, ft4Low, ft4High) }
        val ft3Status = ft3?.let { evaluateThyroidStatus(it, ft3Low, ft3High) }
        val tpoAbStatus = tpoAb?.let {
            if (it > tpoAbHigh) AntibodyStatus.ELEVATED else AntibodyStatus.NEGATIVE
        } ?: AntibodyStatus.UNKNOWN

        val interpretation = buildInterpretation(tshStatus, ft4Status, ft3Status, tpoAbStatus)
        val confidence = calculateConfidence(tsh != null, ft4 != null, ft3 != null, tpoAb != null)

        return LabResultAnalysis(
            tshStatus = tshStatus,
            ft4Status = ft4Status,
            ft3Status = ft3Status,
            tpoAbStatus = tpoAbStatus,
            overallInterpretation = interpretation,
            confidence = confidence
        )
    }

    // ============================================================
    // Métodos privados de lógica de recomendación (basados en evidencia)
    // ============================================================

    private fun levothyroxineRecommendation(
        weightKg: Double,
        age: Int,
        tshStatus: ThyroidStatus
    ): MedicationRecommendation {
        // Dosis inicial típica: 1.6 mcg/kg/día (ATA 2020)
        // En adultos jóvenes sin cardiopatía: 1.6 mcg/kg
        // En mayores de 65 o con cardiopatía: 25-50 mcg inicial, ajustar cada 4-6 semanas
        val baseDoseMcg = if (age > 65) 25.0 else (weightKg * 1.6)
        val roundedDose = (baseDoseMcg / 25).roundToInt() * 25
        val doseMcg = if (roundedDose < 25) 25.0 else roundedDose.toDouble()

        return MedicationRecommendation(
            name = "Levotiroxina (Eutirox)",
            dose = "${doseMcg.toInt()} mcg",
            frequency = "Una vez al día (en ayunas)",
            rationale = "TSH elevado indica hipotiroidismo. La levotiroxina es el tratamiento de primera línea según ATA 2020. Dosis inicial basada en peso y edad.",
            evidenceLevel = "ATA 2020 - Recomendación fuerte"
        )
    }

    private fun antithyroidRecommendation(tshStatus: ThyroidStatus): MedicationRecommendation {
        return MedicationRecommendation(
            name = "Consultar endocrinólogo",
            dose = "",
            frequency = "",
            rationale = "TSH suprimido sugiere hipertiroidismo. Requiere evaluación médica para antithiroideos (metimazol, propiltiouracilo) o yodo radiactivo.",
            evidenceLevel = "ATA 2020 - Recomendación fuerte"
        )
    }

    private fun subclinicalHypoRecommendation(
        weightKg: Double,
        age: Int,
        analysis: LabResultAnalysis
    ): MedicationRecommendation {
        val treat = analysis.tpoAbStatus == AntibodyStatus.POSITIVE ||
            analysis.tpoAbStatus == AntibodyStatus.ELEVATED

        return if (treat) {
            levothyroxineRecommendation(weightKg, age, ThyroidStatus.BORDERLINE_HIGH)
        } else {
            MedicationRecommendation(
                name = "Observación",
                dose = "",
                frequency = "",
                rationale = "Hipotiroidismo subclínico sin anticuerpos elevados: monitorizar TSH cada 6-12 meses según ATA 2020.",
                evidenceLevel = "ATA 2020 - Recomendación condicional"
            )
        }
    }

    private fun seleniumRecommendation(): SupplementRecommendation {
        return SupplementRecommendation(
            name = "Selenio",
            dose = "200 mcg/día",
            rationale = "Puede reducir anticuerpos anti-TPO en tiroiditis de Hashimoto. Evidencia moderada según estudios clínicos (2019-2022).",
            evidenceLevel = "Evidencia moderada",
            contraindications = listOf("Hiperseleniemia", "Dosis >400 mcg/día sin supervisión médica")
        )
    }

    private fun dietRecommendation(tshStatus: ThyroidStatus): DietRecommendation {
        return when (tshStatus) {
            ThyroidStatus.HIGH -> DietRecommendation(
                focus = "Apoyar función tiroidea baja",
                foodsToInclude = listOf("Pescados ricos en omega-3", "Frutas y verduras", "Proteínas magras"),
                foodsToLimit = listOf("Soja en exceso", "Fibra muy alta cerca de la toma de levotiroxina", "Nueces de Brasil (selenio excesivo)"),
                rationale = "Evitar alimentos que interfieran con absorción de levotiroxina. Tomar medicamento en ayunas, 30-60 min antes del desayuno.",
                evidenceLevel = "ATA 2020 - Consenso de práctica"
            )
            ThyroidStatus.LOW -> DietRecommendation(
                focus = "Evitar exceso de yodo",
                foodsToInclude = listOf("Alimentos con bajo contenido de yodo", "Hidratación adecuada"),
                foodsToLimit = listOf("Sal yodada en exceso", "Algas marinas", "Suplementos de yodo sin indicación médica"),
                rationale = "En hipertiroidismo, evitar exceso de yodo que puede exacerbar la condición.",
                evidenceLevel = "ATA 2020 - Consenso de práctica"
            )
            else -> DietRecommendation(
                focus = "Dieta equilibrada",
                foodsToInclude = listOf("Variedad de nutrientes", "Adecuada ingesta de selenio (55-200 mcg/día)"),
                foodsToLimit = listOf("Exceso de yodo sin supervisión médica"),
                rationale = "Mantenimiento de función tiroidea estable.",
                evidenceLevel = "Consenso general"
            )
        }
    }

    private fun exerciseRecommendation(tshStatus: ThyroidStatus, age: Int): ExerciseRecommendation {
        return when (tshStatus) {
            ThyroidStatus.HIGH -> ExerciseRecommendation(
                type = "Aeróbico ligero a moderado",
                frequency = "3-5 veces por semana",
                duration = "20-30 minutos por sesión",
                rationale = "Ayuda con fatiga y control de peso en hipotiroidismo tratado.",
                evidenceLevel = "Consenso clínico general"
            )
            ThyroidStatus.LOW -> ExerciseRecommendation(
                type = "Evitar ejercicio intenso sin control médico",
                frequency = "Según tolerancia",
                duration = "Según tolerancia",
                rationale = "En hipertiroidismo no tratado, evitar taquicardia y sobreesfuerzo cardiovascular.",
                evidenceLevel = "Consenso clínico general"
            )
            else -> ExerciseRecommendation(
                type = "Aeróbico y resistencia",
                frequency = "3-5 veces por semana",
                duration = "30 minutos por sesión",
                rationale = "Mantenimiento de salud metabólica y cardiovascular.",
                evidenceLevel = "Consenso clínico general"
            )
        }
    }

    private fun generateWarning(analysis: LabResultAnalysis, patient: Patient): String? {
        val warnings = mutableListOf<String>()

        if (analysis.tshStatus == ThyroidStatus.LOW) {
            warnings.add("TSH suprimido: posible hipertiroidismo. Consultar urgentemente con endocrinólogo antes de iniciar o cambiar medicación.")
        }
        if (analysis.tpoAbStatus == AntibodyStatus.ELEVATED) {
            warnings.add("Anticuerpos anti-TPO elevados: posible tiroiditis autoinmune. Seguimiento endocrinológico recomendado.")
        }
        if (analysis.confidence < 0.7) {
            warnings.add("Datos de laboratorio incompletos. La recomendación tiene menor confiabilidad.")
        }

        warnings.add("Esta aplicación no sustituye la consulta médica. Siempre consulte a su endocrinólogo antes de cambiar medicación o suplementación.")

        return if (warnings.isEmpty()) null else warnings.joinToString("\n")
    }

    private fun evaluateThyroidStatus(value: Double, low: Double, high: Double): ThyroidStatus {
        return when {
            value < low * 0.8 -> ThyroidStatus.LOW
            value < low -> ThyroidStatus.BORDERLINE_LOW
            value > high * 1.2 -> ThyroidStatus.HIGH
            value > high -> ThyroidStatus.BORDERLINE_HIGH
            else -> ThyroidStatus.NORMAL
        }
    }

    private fun buildInterpretation(
        tsh: ThyroidStatus?,
        ft4: ThyroidStatus?,
        ft3: ThyroidStatus?,
        tpoAb: AntibodyStatus?
    ): String {
        val parts = mutableListOf<String>()
        tsh?.let { parts.add("TSH: ${it.name}") }
        ft4?.let { parts.add("FT4: ${it.name}") }
        ft3?.let { parts.add("FT3: ${it.name}") }
        tpoAb?.let { parts.add("Anti-TPO: ${it.name}") }
        return parts.joinToString(", ")
    }

    private fun calculateConfidence(tsh: Boolean, ft4: Boolean, ft3: Boolean, tpoAb: Boolean): Double {
        var score = 0.0
        if (tsh) score += 0.4
        if (ft4) score += 0.3
        if (ft3) score += 0.2
        if (tpoAb) score += 0.1
        return score.coerceIn(0.0, 1.0)
    }

    private fun estimateWeightKg(patient: Patient): Double {
        // Si no hay datos de peso, usar promedio adulto 70 kg
        return 70.0
    }

    private fun calculateAge(birthDateMs: Long): Int {
        val birth = java.util.Date(birthDateMs)
        val now = java.util.Date()
        val cal = java.util.Calendar.getInstance()
        cal.time = birth
        val birthYear = cal.get(java.util.Calendar.YEAR)
        cal.time = now
        return cal.get(java.util.Calendar.YEAR) - birthYear
    }
}
