package com.jorgeramos.thyroidsupport.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MutableLiveData
import com.jorgeramos.thyroidsupport.domain.Patient
import com.jorgeramos.thyroidsupport.domain.Symptom
import com.jorgeramos.thyroidsupport.domain.repository.PatientRepository
import com.jorgeramos.thyroidsupport.domain.repository.SymptomRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val symptomRepository: SymptomRepository
) : ViewModel() {

    private val _currentPatient = MutableLiveData<Patient?>(null)
    val currentPatient = _currentPatient.asLiveData()

    private val _symptoms = MutableLiveData<List<Symptom>>(emptyList())
    val symptoms = _symptoms.asLiveData()

    init {
        loadPatient()
    }

    fun loadPatient() {
        viewModelScope.launch {
            val patient = patientRepository.getCurrentPatient()
            _currentPatient.value = patient
            if (patient != null) {
                loadSymptoms(patient.id)
            }
        }
    }

    private fun loadSymptoms(patientId: Long) {
        viewModelScope.launch {
            val list = symptomRepository.getSymptomsForPatient(patientId)
            _symptoms.value = list
        }
    }

    fun addSymptom(symptom: Symptom) {
        viewModelScope.launch {
            symptomRepository.insert(symptom)
            symptom.patientId?.let { loadSymptoms(it) }
        }
    }
}
