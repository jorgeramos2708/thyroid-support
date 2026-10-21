package com.jorgeramos.thyroidsupport.presentation

import androidx.hilt.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MutableLiveData
import com.jorgeramos.thyroidsupport.domain.SymptomAdvice
import com.jorgeramos.thyroidsupport.domain.SymptomAdviceData
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdviceViewModel @Inject constructor() : ViewModel() {

    private val _selectedSymptoms = MutableLiveData<List<String>>(emptyList())
    val selectedSymptoms = _selectedSymptoms.asLiveData()

    private val _activeAdvices = MutableLiveData<List<SymptomAdvice>>(emptyList())
    val activeAdvices = _activeAdvices.asLiveData()

    private val _warningAdvice = MutableLiveData(SymptomAdviceData.warningAdvice)
    val warningAdvice = _warningAdvice.asLiveData()

    private val _hasWarning = MutableLiveData(false)
    val hasWarning = _hasWarning.asLiveData()

    init {
        updateAdvices()
    }

    fun toggleSymptom(symptomId: String) {
        val current = _selectedSymptoms.value ?: emptyList()
        val newList = if (current.contains(symptomId)) {
            current.filter { it != symptomId }
        } else {
            current + symptomId
        }
        _selectedSymptoms.value = newList
        updateAdvices()
    }

    fun isSymptomSelected(symptomId: String): Boolean {
        return (_selectedSymptoms.value ?: emptyList()).contains(symptomId)
    }

    fun getSelectedCount(): Int {
        return (_selectedSymptoms.value ?: emptyList()).size
    }

    private fun updateAdvices() {
        val selected = _selectedSymptoms.value ?: emptyList()
        val advices = SymptomAdviceData.allAdvices.filter { symptom -> selected.contains(symptom.symptomId) }
        _activeAdvices.value = advices

        // Determinar si hay advertencia basada en severidad alta
        val hasWarning = advices.any { it.severity >= 7 }
        _hasWarning.value = hasWarning
    }

    fun getSeverityColor(severity: Int): androidx.compose.ui.graphics.Color {
        return when {
            severity <= 3 -> MaterialThemeColor.green
            severity <= 6 -> MaterialThemeColor.amber
            else -> MaterialThemeColor.red
        }
    }

    private object MaterialThemeColor {
        val green = androidx.compose.ui.graphics.Color(0xFF2E7D32)
        val amber = androidx.compose.ui.graphics.Color(0xFFF9A825)
        val red = androidx.compose.ui.graphics.Color(0xFFC62828)
    }
}
