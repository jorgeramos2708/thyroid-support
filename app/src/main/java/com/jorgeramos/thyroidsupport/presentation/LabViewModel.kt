package com.jorgeramos.thyroidsupport.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MutableLiveData
import com.jorgeramos.thyroidsupport.domain.Lab
import com.jorgeramos.thyroidsupport.domain.repository.LabRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class LabViewModel @Inject constructor(
    private val labRepository: LabRepository
) : ViewModel() {

    private val _labs = MutableLiveData<List<Lab>>(emptyList())
    val labs = _labs.asLiveData()

    fun loadLabs(patientId: Long) {
        viewModelScope.launch {
            val list = labRepository.getLabsForPatient(patientId)
            _labs.value = list
        }
    }

    fun addLab(lab: Lab) {
        viewModelScope.launch {
            labRepository.insert(lab)
            loadLabs(lab.patientId)
        }
    }

    fun deleteLab(id: Long) {
        viewModelScope.launch {
            labRepository.delete(id)
        }
    }
}
