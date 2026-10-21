package com.jorgeramos.thyroidsupport.presentation

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
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
    val entry: LiveData<DailySymptomEntry?> = _entry

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

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
                    doctorNote = doctorNote?.takeIf { it.isNotBlank() }
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
