package com.jorgeramos.thyroidsupport.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgeramos.thyroidsupport.domain.Symptom
import com.jorgeramos.thyroidsupport.domain.repository.SymptomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SymptomViewModel @Inject constructor(
    private val symptomRepository: SymptomRepository
) : ViewModel() {

    fun saveSymptom(symptom: Symptom) {
        viewModelScope.launch {
            symptomRepository.insert(symptom)
        }
    }
}
