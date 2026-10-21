package com.jorgeramos.thyroidsupport.presentation

import androidx.hilt.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MutableLiveData
import com.jorgeramos.thyroidsupport.domain.DailySymptomEntry
import com.jorgeramos.thyroidsupport.domain.repository.DailySymptomRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailySymptomViewModel @Inject constructor(
    private val dailySymptomRepository: DailySymptomRepository
) : ViewModel() {

    private val _entry = MutableLiveData<DailySymptomEntry?>(null)
    val entry = _entry.asLiveData()

    private val _loading = MutableLiveData(false)
    val loading = _loading.asLiveData()

    private val _error = MutableLiveData<String?>(null)
    val error = _error.asLiveData()

    fun loadLatest() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val latest = dailySymptomRepository.getLatestEntry()
                _entry.value = latest
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun saveEntry(
        fatigue: Int,
        coldSensitivity: Int,
        palpitations: Int,
        lowMood: Int,
        sleepProblems: Int,
        weightKg: Double,
        heartRate: Int,
        doctorNote: String?
    ) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val now = System.currentTimeMillis()
                val entry = DailySymptomEntry(
                    date = now,
                    fatigue = fatigue,
                    coldSensitivity = coldSensitivity,
                    palpitations = palpitations,
                    lowMood = lowMood,
                    sleepProblems = sleepProblems,
                    weightKg = weightKg,
                    heartRate = heartRate,
                    doctorNote = doctorNote.takeIf { it.isNotBlank() }.orNull()
                )
                val id = dailySymptomRepository.insert(entry)
                _entry.value = entry.copy(id = id)
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }
}
