package com.jorgeramos.thyroidsupport.presentation.onboarding

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TreatmentSetup(
    val medicationName: String = "",
    val dose: String = "",
    val time: String = "7:00 am",
    val dailyReminder: Boolean = true,
    val tshMin: String = "0.5",
    val tshMax: String = "2.5",
    val nextLabDate: String? = null,
    val nextMedicalAppointment: String? = null
)

@HiltViewModel
class OnboardingStep2ViewModel @Inject constructor() : ViewModel() {

    private val _treatment = MutableLiveData<TreatmentSetup>(TreatmentSetup())
    val treatment: LiveData<TreatmentSetup> = _treatment

    private val _isValid = MutableLiveData(false)
    val isValid: LiveData<Boolean> = _isValid

    init {
        validate()
    }

    fun updateMedicationName(name: String) {
        val current = _treatment.value ?: TreatmentSetup()
        _treatment.value = current.copy(medicationName = name.trim())
        validate()
    }

    fun updateDose(dose: String) {
        val current = _treatment.value ?: TreatmentSetup()
        _treatment.value = current.copy(dose = dose.trim())
        validate()
    }

    fun updateTime(time: String) {
        val current = _treatment.value ?: TreatmentSetup()
        _treatment.value = current.copy(time = time)
        validate()
    }

    fun toggleDailyReminder(checked: Boolean) {
        val current = _treatment.value ?: TreatmentSetup()
        _treatment.value = current.copy(dailyReminder = checked)
    }

    fun updateTshMin(value: String) {
        val current = _treatment.value ?: TreatmentSetup()
        _treatment.value = current.copy(tshMin = value)
        validate()
    }

    fun updateTshMax(value: String) {
        val current = _treatment.value ?: TreatmentSetup()
        _treatment.value = current.copy(tshMax = value)
        validate()
    }

    fun updateNextLabDate(date: String) {
        val current = _treatment.value ?: TreatmentSetup()
        _treatment.value = current.copy(nextLabDate = date)
    }

    fun updateNextMedicalAppointment(date: String) {
        val current = _treatment.value ?: TreatmentSetup()
        _treatment.value = current.copy(nextMedicalAppointment = date)
    }

    private fun validate() {
        val treatment = _treatment.value
        _isValid.value = treatment != null &&
            treatment.medicationName.isNotBlank() &&
            treatment.dose.isNotBlank() &&
            treatment.time.isNotBlank()
    }
}
