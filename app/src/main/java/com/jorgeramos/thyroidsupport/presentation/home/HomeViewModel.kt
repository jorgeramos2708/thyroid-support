package com.jorgeramos.thyroidsupport.presentation.home

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class WeekDay(
    val name: String,
    val number: Int,
    val isSelected: Boolean = false,
    val isToday: Boolean = false
)

data class NextDose(
    val time: String,
    val medication: String,
    val dose: String,
    val instructions: String,
    val isTaken: Boolean = false
)

data class TodayItem(
    val icon: String,
    val title: String,
    val subtitle: String,
    val time: String
)

data class NextControl(
    val title: String,
    val subtitle: String
)

data class QuickAction(
    val label: String,
    val icon: String
)

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _currentWeek = MutableLiveData<List<WeekDay>>(emptyList())
    val currentWeek: LiveData<List<WeekDay>> = _currentWeek

    private val _nextDose = MutableLiveData<NextDose?>(null)
    val nextDose: LiveData<NextDose?> = _nextDose

    private val _todayItems = MutableLiveData<List<TodayItem>>(emptyList())
    val todayItems: LiveData<List<TodayItem>> = _todayItems

    private val _nextControl = MutableLiveData<NextControl?>(null)
    val nextControl: LiveData<NextControl?> = _nextControl

    private val _quickActions = MutableLiveData<List<QuickAction>>(emptyList())
    val quickActions: LiveData<List<QuickAction>> = _quickActions

    init {
        setupWeek()
        setupNextDose()
        setupTodayItems()
        setupNextControl()
        setupQuickActions()
    }

    private fun setupWeek() {
        val days = listOf(
            WeekDay("L", 19, isToday = false),
            WeekDay("M", 20, isSelected = true, isToday = true),
            WeekDay("X", 21, isToday = false),
            WeekDay("J", 22, isToday = false),
            WeekDay("V", 23, isToday = false),
            WeekDay("S", 24, isToday = false),
            WeekDay("D", 25, isToday = false)
        )
        _currentWeek.value = days
    }

    private fun setupNextDose() {
        _nextDose.value = NextDose(
            time = "7:00 am",
            medication = "Levotiroxina",
            dose = "75 mcg",
            instructions = "en ayunas",
            isTaken = true
        )
    }

    private fun setupTodayItems() {
        _todayItems.value = listOf(
            TodayItem(
                icon = "leaf",
                title = "Desayuno",
                subtitle = "Espera 30–60 min",
                time = "7:45"
            ),
            TodayItem(
                icon = "attachment",
                title = "Suplementación",
                subtitle = "Separada de la dosis",
                time = "1:00 pm"
            )
        )
    }

    private fun setupNextControl() {
        _nextControl.value = NextControl(
            title = "Laboratorio en 3 semanas",
            subtitle = "Cita con endocrinología · 12 nov"
        )
    }

    private fun setupQuickActions() {
        _quickActions.value = listOf(
            QuickAction("Síntomas", "alert_circle"),
            QuickAction("Mi evolución", "science"),
            QuickAction("Ejercicio", "fitness_center"),
            QuickAction("Consejos", "lightbulb"),
            QuickAction("Exportar", "share"),
            QuickAction("Asistente", "smart_toy")
        )
    }

    fun selectDay(day: WeekDay) {
        val updated = _currentWeek.value?.map { d ->
            d.copy(isSelected = d.number == day.number)
        } ?: emptyList()
        _currentWeek.value = updated
    }

    fun toggleDoseTaken() {
        val current = _nextDose.value
        _nextDose.value = current?.copy(isTaken = !current.isTaken)
    }
}
