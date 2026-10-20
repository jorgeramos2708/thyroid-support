package com.jorgeramos.thyroidsupport.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgeramos.thyroidsupport.domain.LabResultAnalysis
import com.jorgeramos.thyroidsupport.domain.Patient
import com.jorgeramos.thyroidsupport.domain.Recommendation
import com.jorgeramos.thyroidsupport.domain.repository.PatientRepository
import com.jorgeramos.thyroidsupport.domain.repository.RecommendationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendationViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val recommendationRepository: RecommendationRepository
) : ViewModel() {

    private val _recommendation = MutableLiveData<Recommendation?>(null)
    val recommendation: LiveData<Recommendation?> = _recommendation

    private val _analysis = MutableLiveData<LabResultAnalysis?>(null)
    val analysis: LiveData<LabResultAnalysis?> = _analysis

    private val _patient = MutableLiveData<Patient?>(null)
    val patient: LiveData<Patient?> = _patient

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    init {
        loadPatient()
    }

    fun loadPatient() {
        viewModelScope.launch {
            val p = patientRepository.getCurrentPatient()
            _patient.value = p
            if (p != null) {
                // Nota: aquí se necesitaría el último lab para generar recomendación completa
                _recommendation.value = null
            }
        }
    }

    fun generateRecommendation(
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
    ) {
        viewModelScope.launch {
            _loading.value = true
            val patient = patientRepository.getCurrentPatient()
            val analysis = recommendationRepository.analyzeLabResults(
                tsh, ft4, ft3, tpoAb,
                tshRefLow, tshRefHigh,
                ft4RefLow, ft4RefHigh,
                ft3RefLow, ft3RefHigh,
                tpoAbRefHigh
            )
            _analysis.value = analysis
            if (patient != null) {
                val rec = recommendationRepository.generateRecommendation(patient, analysis)
                _recommendation.value = rec
            }
            _patient.value = patient
            _loading.value = false
        }
    }
}
