package com.jorgeramos.thyroidsupport.presentation

import androidx.hilt.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MutableLiveData
import com.jorgeramos.thyroidsupport.domain.Medication
import com.jorgeramos.thyroidsupport.domain.MedicationLog
import com.jorgeramos.thyroidsupport.domain.repository.MedicationLogRepository
import com.jorgeramos.thyroidsupport.domain.repository.MedicationRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MedicationDay(
    val dayLetter: String,
    val dayNumber: Int,
    val isTaken: Boolean,
    val isToday: Boolean
)

data class MedicationState(
    val medication: Medication?,
    val logs: List<MedicationLog>,
    val consistencyPercentage: Int
)

@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val medicationRepository: MedicationRepository,
    private val medicationLogRepository: MedicationLogRepository
) : ViewModel() {

    private val _medicationState = MutableLiveData<MedicationState?>(null)
    val medicationState = _medicationState.asLiveData()

    private val _loading = MutableLiveData(false)
    val loading = _loading.asLiveData()

    private val _error = MutableLiveData<String?>(null)
    val error = _error.asLiveData()

    fun loadMedication() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val medications = medicationRepository.getMedicationsForPatient(1)
                val medication = medications.firstOrNull()

                val logs = if (medication != null) {
                    medicationLogRepository.getLogsForMedication(medication.id!!)
                } else {
                    emptyList()
                }

                val consistency = calculateConsistency(logs)

                _medicationState.value = MedicationState(medication, logs, consistency)
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun toggleLogForDate(medicationId: Long, date: Long, isTaken: Boolean) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val existingLog = medicationLogRepository.getLogForDate(medicationId, date)
                if (existingLog != null) {
                    medicationLogRepository.insert(existingLog.copy(taken = isTaken))
                } else {
                    medicationLogRepository.insert(MedicationLog(
                        medicationId = medicationId,
                        date = date,
                        taken = isTaken
                    ))
                }
                loadMedication()
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    private fun calculateConsistency(logs: List<MedicationLog>): Int {
        if (logs.isEmpty()) return 0
        val takenCount = logs.count { it.taken }
        return (takenCount * 100) / logs.size
    }

    fun getDaysOfWeekLogs(logs: List<MedicationLog>): List<MedicationDay> {
        val now = System.currentTimeMillis()
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = now

        val days = mutableListOf<MedicationDay>()
        val dayNames = listOf("D", "L", "M", "X", "J", "V", "S")

        for (i in 6 downTo 0) {
            calendar.timeInMillis = now - (i * 24 * 60 * 60 * 1000)
            val dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK)
            val dayIndex = (dayOfWeek + 5) % 7
            val dayNumber = calendar.get(java.util.Calendar.DAY_OF_MONTH)
            val dayLetter = dayNames[dayIndex]
            val isToday = i == 0

            val logForDate = logs.find { log ->
                val logCalendar = java.util.Calendar.getInstance()
                logCalendar.timeInMillis = log.date
                logCalendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
                logCalendar.set(java.util.Calendar.MINUTE, 0)
                logCalendar.set(java.util.Calendar.SECOND, 0)
                logCalendar.set(java.util.Calendar.MILLISECOND, 0)

                val currentCalendar = java.util.Calendar.getInstance()
                currentCalendar.timeInMillis = calendar.timeInMillis
                currentCalendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
                currentCalendar.set(java.util.Calendar.MINUTE, 0)
                currentCalendar.set(java.util.Calendar.SECOND, 0)
                currentCalendar.set(java.util.Calendar.MILLISECOND, 0)

                logCalendar.timeInMillis == currentCalendar.timeInMillis
            }

            days.add(MedicationDay(
                dayLetter = dayLetter,
                dayNumber = dayNumber,
                isTaken = logForDate?.taken ?: false,
                isToday = isToday
            ))
        }

        return days
    }
}
