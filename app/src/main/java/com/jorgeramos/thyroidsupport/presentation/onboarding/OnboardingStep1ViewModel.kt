package com.jorgeramos.thyroidsupport.presentation.onboarding

import androidx.hilt.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PatientProfile(
    val name: String = "",
    val birthYear: Int? = null,
    val condition: Condition? = null,
    val hasPregnancy: Boolean = false,
    val hasHeartProblems: Boolean = false,
    val hasOsteoporosis: Boolean = false
)

enum class Condition {
    HYPOTHYROIDISM("Hipotiroidismo", "Tiroides poco activa"),
    HYPERTHYROIDISM("Hipertiroidismo", "Tiroides muy activa"),
    OTHER("Otra o no lo sé", "Verás consejos generales")

    val title: String
    val subtitle: String
}

@HiltViewModel
class OnboardingStep1ViewModel @Inject constructor() : ViewModel() {

    private val _profile = MutableLiveData<PatientProfile>(PatientProfile())
    val profile = _profile.asLiveData()

    private val _selectedCondition = MutableLiveData<Condition?>(null)
    val selectedCondition = _selectedCondition.asLiveData()

    private val _isValid = MutableLiveData(false)
    val isValid = _isValid.asLiveData()

    init {
        _selectedCondition.value = Condition.HYPOTHYROIDISM
        updateProfile()
    }

    fun updateName(name: String) {
        val current = _profile.value ?: PatientProfile()
        _profile.value = current.copy(name = name.trim())
        validate()
    }

    fun updateBirthYear(year: Int) {
        val current = _profile.value ?: PatientProfile()
        _profile.value = current.copy(birthYear = year)
        validate()
    }

    fun selectCondition(condition: Condition) {
        _selectedCondition.value = condition
        val current = _profile.value ?: PatientProfile()
        _profile.value = current.copy(condition = condition)
        validate()
    }

    fun togglePregnancy(checked: Boolean) {
        val current = _profile.value ?: PatientProfile()
        _profile.value = current.copy(hasPregnancy = checked)
    }

    fun toggleHeartProblems(checked: Boolean) {
        val current = _profile.value ?: PatientProfile()
        _profile.value = current.copy(hasHeartProblems = checked)
    }

    fun toggleOsteoporosis(checked: Boolean) {
        val current = _profile.value ?: PatientProfile()
        _profile.value = current.copy(hasOsteoporosis = checked)
    }

    private fun validate() {
        val profile = _profile.value
        _isValid.value = profile?.name.isNotEmpty() == true &&
            profile.birthYear != null &&
            profile.condition != null
    }
}
