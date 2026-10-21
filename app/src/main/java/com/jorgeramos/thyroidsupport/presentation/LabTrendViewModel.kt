package com.jorgeramos.thyroidsupport.presentation

import androidx.hilt.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MutableLiveData
import com.jorgeramos.thyroidsupport.domain.LabTrendEntry
import com.jorgeramos.thyroidsupport.domain.repository.LabTrendRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LabTrendPoint(
    val testType: String,
    val value: Double,
    val testDate: Long
)

@HiltViewModel
class LabTrendViewModel @Inject constructor(
    private val labTrendRepository: LabTrendRepository
) : ViewModel() {

    private val _trends = MutableLiveData<List<LabTrendEntry>>(emptyList())
    val trends = _trends.asLiveData()

    private val _loading = MutableLiveData(false)
    val loading = _loading.asLiveData()

    private val _error = MutableLiveData<String?>(null)
    val error = _error.asLiveData()

    fun loadTrends() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val trends = labTrendRepository.getAllTrends()
                _trends.value = trends
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun addTrend(testType: String, value: Double, testDate: Long, referenceLow: Double?, referenceHigh: Double?, unit: String?) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val entry = LabTrendEntry(
                    testType = testType,
                    value = value,
                    testDate = testDate,
                    referenceLow = referenceLow,
                    referenceHigh = referenceHigh,
                    unit = unit
                )
                labTrendRepository.insert(entry)
                loadTrends()
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun getTrendsForType(testType: String): List<LabTrendEntry> {
        val all = _trends.value ?: emptyList()
        return all.filter { it.testType == testType }
    }

    fun deleteTrend(id: Long) {
        viewModelScope.launch {
            _loading.value = true
            try {
                labTrendRepository.delete(id)
                loadTrends()
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }
}
