package com.jorgeramos.thyroidsupport.data.repository

import com.jorgeramos.thyroidsupport.domain.Patient
import com.jorgeramos.thyroidsupport.domain.LabResultAnalysis
import com.jorgeramos.thyroidsupport.domain.ThyroidStatus
import com.jorgeramos.thyroidsupport.domain.AntibodyStatus
import com.jorgeramos.thyroidsupport.domain.repository.RecommendationRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RecommendationRepositoryImplTest {

    private val repo: RecommendationRepository = RecommendationRepositoryImpl()

    @Test
    fun `analiza TSH alto como hipotiroidismo`() = runTest {
        val analysis = repo.analyzeLabResults(
            tsh = 8.5,
            ft4 = null,
            ft3 = null,
            tpoAb = null,
            tshRefLow = 0.4,
            tshRefHigh = 4.0,
            ft4RefLow = null,
            ft4RefHigh = null,
            ft3RefLow = null,
            ft3RefHigh = null,
            tpoAbRefHigh = null
        )

        assertEquals(ThyroidStatus.HIGH, analysis.tshStatus)
        assertTrue(analysis.overallInterpretation.contains("TSH: HIGH"))
    }

    @Test
    fun `analiza TSH bajo como hipertiroidismo`() = runTest {
        val analysis = repo.analyzeLabResults(
            tsh = 0.05,
            ft4 = null,
            ft3 = null,
            tpoAb = null,
            tshRefLow = 0.4,
            tshRefHigh = 4.0,
            ft4RefLow = null,
            ft4RefHigh = null,
            ft3RefLow = null,
            ft3RefHigh = null,
            tpoAbRefHigh = null
        )

        assertEquals(ThyroidStatus.LOW, analysis.tshStatus)
    }

    @Test
    fun `analiza TSH normal`() = runTest {
        val analysis = repo.analyzeLabResults(
            tsh = 2.5,
            ft4 = null,
            ft3 = null,
            tpoAb = null,
            tshRefLow = 0.4,
            tshRefHigh = 4.0,
            ft4RefLow = null,
            ft4RefHigh = null,
            ft3RefLow = null,
            ft3RefHigh = null,
            tpoAbRefHigh = null
        )

        assertEquals(ThyroidStatus.NORMAL, analysis.tshStatus)
    }

    @Test
    fun `detecta anti-TPO elevados`() = runTest {
        val analysis = repo.analyzeLabResults(
            tsh = 5.0,
            ft4 = null,
            ft3 = null,
            tpoAb = 150.0,
            tshRefLow = 0.4,
            tshRefHigh = 4.0,
            ft4RefLow = null,
            ft4RefHigh = null,
            ft3RefLow = null,
            ft3RefHigh = null,
            tpoAbRefHigh = 35.0
        )

        assertEquals(AntibodyStatus.ELEVATED, analysis.tpoAbStatus)
    }

    @Test
    fun `genera recomendación para hipotiroidismo con levotiroxina`() = runTest {
        val patient = Patient(id = 1, name = "María", birthDate = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 35, gender = "F")
        val analysis = LabResultAnalysis(
            tshStatus = ThyroidStatus.HIGH,
            ft4Status = null,
            ft3Status = null,
            tpoAbStatus = null,
            overallInterpretation = "TSH: HIGH",
            confidence = 0.5
        )

        val rec = repo.generateRecommendation(patient, analysis)

        assertNotNull(rec.medication)
        assertEquals("Levotiroxina (Eutirox)", rec.medication!!.name)
        assertTrue(rec.medication!!.dose.contains("mcg"))
        assertTrue(rec.medication!!.evidenceLevel.contains("ATA 2020") || rec.sources.any { it.contains("ATA") })
    }

    @Test
    fun `genera recomendación de selenio para anti-TPO positivos`() = runTest {
        val patient = Patient(id = 1, name = "María", birthDate = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 35, gender = "F")
        val analysis = LabResultAnalysis(
            tshStatus = ThyroidStatus.NORMAL,
            ft4Status = null,
            ft3Status = null,
            tpoAbStatus = AntibodyStatus.POSITIVE,
            overallInterpretation = "Anti-TPO: POSITIVE",
            confidence = 0.4
        )

        val rec = repo.generateRecommendation(patient, analysis)

        assertNotNull(rec.supplement)
        assertEquals("Selenio", rec.supplement!!.name)
        assertTrue(rec.supplement!!.dose.contains("200 mcg"))
    }

    @Test
    fun `incluye advertencia para TSH suprimido`() = runTest {
        val patient = Patient(id = 1, name = "María", birthDate = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 35, gender = "F")
        val analysis = LabResultAnalysis(
            tshStatus = ThyroidStatus.LOW,
            ft4Status = null,
            ft3Status = null,
            tpoAbStatus = null,
            overallInterpretation = "TSH: LOW",
            confidence = 0.5
        )

        val rec = repo.generateRecommendation(patient, analysis)

        assertNotNull(rec.warning)
        assertTrue(rec.warning!!.contains("endocrinólogo"))
    }

    @Test
    fun `confianza baja con pocos datos`() = runTest {
        val analysis = repo.analyzeLabResults(
            tsh = 2.5,
            ft4 = null,
            ft3 = null,
            tpoAb = null,
            tshRefLow = 0.4,
            tshRefHigh = 4.0,
            ft4RefLow = null,
            ft4RefHigh = null,
            ft3RefLow = null,
            ft3RefHigh = null,
            tpoAbRefHigh = null
        )

        assertEquals(0.4, analysis.confidence, 0.001)
    }

    @Test
    fun `confianza alta con todos los datos`() = runTest {
        val analysis = repo.analyzeLabResults(
            tsh = 2.5,
            ft4 = 15.0,
            ft3 = 5.0,
            tpoAb = 10.0,
            tshRefLow = 0.4,
            tshRefHigh = 4.0,
            ft4RefLow = 9.0,
            ft4RefHigh = 19.0,
            ft3RefLow = 3.5,
            ft3RefHigh = 6.5,
            tpoAbRefHigh = 35.0
        )

        assertEquals(1.0, analysis.confidence, 0.001)
    }
}
