package com.jorgeramos.thyroidsupport.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MutableLiveData
import com.jorgeramos.thyroidsupport.domain.MealPlanEntry
import com.jorgeramos.thyroidsupport.domain.repository.MealPlanRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MealPlanViewModel @Inject constructor(
    private val mealPlanRepository: MealPlanRepository
) : ViewModel() {

    private val _entries = MutableLiveData<List<MealPlanEntry>>(emptyList())
    val entries = _entries.asLiveData()

    private val _loading = MutableLiveData(false)
    val loading = _loading.asLiveData()

    private val _error = MutableLiveData<String?>(null)
    val error = _error.asLiveData()

    fun loadEntries(startDate: Long, endDate: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val list = mealPlanRepository.getEntriesForDateRange(startDate, endDate)
                _entries.value = list
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun addEntry(entry: MealPlanEntry) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                mealPlanRepository.insert(entry)
                // Recargar para refrescar lista
                val now = System.currentTimeMillis()
                val start = now - 30L * 24 * 60 * 60 * 1000 // últimos 30 días
                loadEntries(start, now)
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }
}
