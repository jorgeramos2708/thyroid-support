package com.jorgeramos.thyroidsupport.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgeramos.thyroidsupport.domain.Medication
import com.jorgeramos.thyroidsupport.domain.repository.MedicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val medicationRepository: MedicationRepository
) : ViewModel() {

    private val _medications = MutableLiveData<List<Medication>>(emptyList())
    val medications: LiveData<List<Medication>> = _medications

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
