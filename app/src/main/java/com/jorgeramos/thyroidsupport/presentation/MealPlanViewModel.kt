package com.jorgeramos.thyroidsupport.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgeramos.thyroidsupport.domain.MealPlanEntry
import com.jorgeramos.thyroidsupport.domain.repository.MealPlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealPlanViewModel @Inject constructor(
    private val mealPlanRepository: MealPlanRepository
) : ViewModel() {

    private val _entries = MutableLiveData<List<MealPlanEntry>>(emptyList())
    val entries: LiveData<List<MealPlanEntry>> = _entries

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

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
