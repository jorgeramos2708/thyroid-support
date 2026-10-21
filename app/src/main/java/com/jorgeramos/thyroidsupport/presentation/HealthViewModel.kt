package com.jorgeramos.thyroidsupport.presentation

import androidx.hilt.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgeramos.thyroidsupport.domain.HealthGroups
import com.jorgeramos.thyroidsupport.domain.HealthPreference
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor() : ViewModel() {

    // Estado actual
    private var flags: MutableSet<String> = mutableSetOf()
    private var otherNote: String = ""

    // Grupo actualmente abierto (null = ninguno)
    private var openGroupId: String? = HealthGroups.allGroups.first { it.id == "digestivo" }.id

    // Estado expuesto
    val groups = HealthGroups.allGroups

    fun getOpenGroupId(): String? = openGroupId

    fun getFlags(): Set<String> = flags.toSet()

    fun getOtherNote(): String = otherNote

    fun toggleGroup(groupId: String) {
        if (openGroupId == groupId) {
            openGroupId = null
        } else {
            openGroupId = groupId
        }
    }

    fun toggleOption(optionId: String) {
        // Si se selecciona "Ninguna" o "No lo sé", desmarcar todo lo demás
        if (optionId == HealthGroups.NINGUNA || optionId == HealthGroups.NO_LO_SE) {
            if (flags.contains(optionId)) {
                flags.remove(optionId)
            } else {
                flags.clear()
                flags.add(optionId)
            }
        } else {
            // Si se selecciona una opción normal, desmarcar "Ninguna" y "No lo sé"
            flags.remove(HealthGroups.NINGUNA)
            flags.remove(HealthGroups.NO_LO_SE)

            if (flags.contains(optionId)) {
                flags.remove(optionId)
            } else {
                flags.add(optionId)
            }
        }
    }

    fun isOptionSelected(optionId: String): Boolean = flags.contains(optionId)

    fun updateOtherNote(text: String) {
        otherNote = text.take(120)
    }

    fun getSelectedCount(groupId: String): Int {
        val group = HealthGroups.allGroups.first { it.id == groupId }
        return group.options.count { flags.contains(it.id) }
    }

    fun getPreference(): HealthPreference {
        return HealthPreference(flags = flags.toSet(), otherNote = otherNote)
    }

    fun setPreference(preference: HealthPreference) {
        flags = mutableSetOf(preference.flags)
        otherNote = preference.otherNote
    }
}
