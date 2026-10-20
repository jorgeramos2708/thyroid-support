package com.jorgeramos.thyroidsupport.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MutableLiveData
import com.jorgeramos.thyroidsupport.domain.Medication
import com.jorgeramos.thyroidsupport.domain.repository.MedicationRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MedicationViewModel @Inject constructor(
    private val medicationRepository: MedicationRepository
) : ViewModel() {

    private val _medications = MutableLiveData<List<Medication>>(emptyList())
    val medications = _medications.asLiveData()

    fun loadMedications(patientId: Long) {
        viewModelScope.launch {
            val list = medicationRepository.getMedicationsForPatient(patientId)
            _medications.value = list
        }
    }

    fun addMedication(medication: Medication) {
        viewModelScope.launch {
            medicationRepository.insert(medication)
            loadMedications(medication.patientId)
        }
    }

    fun deleteMedication(id: Long) {
        viewModelScope.launch {
            medicationRepository.delete(id)
        }
    }
}
