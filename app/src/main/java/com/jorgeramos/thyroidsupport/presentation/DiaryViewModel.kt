package com.jorgeramos.thyroidsupport.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MutableLiveData
import com.jorgeramos.thyroidsupport.domain.DiaryEntry
import com.jorgeramos.thyroidsupport.domain.repository.DiaryRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiaryViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
) : ViewModel() {

    private val _entries = MutableLiveData<List<DiaryEntry>>(emptyList())
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
                val list = diaryRepository.getEntriesForDateRange(startDate, endDate)
                _entries.value = list
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun addEntry(entry: DiaryEntry) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                diaryRepository.insert(entry)
                val now = System.currentTimeMillis()
                val start = now - 30L * 24 * 60 * 60 * 1000
                loadEntries(start, now)
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }
}
