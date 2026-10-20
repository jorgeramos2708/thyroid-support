package com.jorgeramos.thyroidsupport.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgeramos.thyroidsupport.domain.Lab
import com.jorgeramos.thyroidsupport.domain.repository.LabRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabViewModel @Inject constructor(
    private val labRepository: LabRepository
) : ViewModel() {

    private val _labs = MutableLiveData<List<Lab>>(emptyList())
    val labs: LiveData<List<Lab>> = _labs

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
