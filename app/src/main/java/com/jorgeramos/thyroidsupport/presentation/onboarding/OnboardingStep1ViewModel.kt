package com.jorgeramos.thyroidsupport.presentation.onboarding

import androidx.hilt.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PatientProfile(
    val name: String = "",
    val birthYear: Int? = null,
    val conditionType: ConditionType? = null,
    val conditionSubtype: ConditionSubtype? = null,
    val hasPregnancy: Boolean = false,
    val hasHeartProblems: Boolean = false,
    val hasOsteoporosis: Boolean = false
)

enum class ConditionType(
    val title: String,
    val subtitle: String
) {
    HYPOTHYROIDISM("Hipotiroidismo", "Tiroides poco activa"),
    HYPERTHYROIDISM("Hipertiroidismo", "Tiroides muy activa"),
    OTHER("Otra o no lo sé", "Verás consejos generales")
}

enum class ConditionSubtype(
    val title: String,
    val description: String
) {
    // Hipotiroidismo subtipos
    HYPOTHYROIDISM_PRIMARY(
        "Hipotiroidismo primario",
        "Tipo más común (más del 90-95% de los casos). Ocurre cuando la propia glándula tiroides está dañada o no funciona bien y produce pocas hormonas (T3 y T4). La causa principal es la tiroiditis de Hashimoto (enfermedad autoinmune), aunque también puede deberse a cirugías o tratamientos con yodo."
    ),
    HYPOTHYROIDISM_SECONDARY(
        "Hipotiroidismo secundario",
        "Se origina cuando la glándula hipófisis (pituitaria) no produce suficiente hormona estimulante de la tiroides (TSH), por lo que la tiroides no recibe la orden de fabricar sus hormonas."
    ),
    HYPOTHYROIDISM_TERTIARY(
        "Hipotiroidismo terciario",
        "Se produce por una falla en el hipotálamo, que no secreta la hormona liberadora de tirotropina (TRH) necesaria para activar a la hipófisis."
    ),
    HYPOTHYROIDISM_SUBCLINICAL(
        "Hipotiroidismo subclínico",
        "Forma leve en la que los niveles de TSH en sangre están elevados, pero las hormonas tiroideas libres (T4) se mantienen normales. La persona no presenta síntomas o son muy escasos."
    ),
    HYPOTHYROIDISM_CONGENITAL(
        "Hipotiroidismo congénito",
        "Está presente desde el nacimiento debido a que la tiroides no se desarrolló bien o no funciona desde la gestación."
    ),
    // Hipertiroidismo subtipos
    HYPERTHYROIDISM_PRIMARY(
        "Hipertiroidismo primario",
        "El más común. Ocurre cuando la propia glándula tiroides está enferma o hiperactiva y produce demasiadas hormonas T3 y T4, lo que provoca que la TSH esté baja o suprimida."
    ),
    HYPERTHYROIDISM_SECONDARY(
        "Hipertiroidismo secundario",
        "Muy poco frecuente. Se origina por un problema en la glándula pituitaria (hipófisis), la cual produce un exceso de TSH, estimulando en exceso a la tiroides."
    ),
    HYPERTHYROIDISM_TERTIARY(
        "Hipertiroidismo terciario",
        "Provocado por una alteración en el hipotálamo (hormona liberadora de TRH). En la práctica clínica se agrupa habitualmente con las alteraciones centrales o secundarias."
    ),
    HYPERTHYROIDISM_SUBCLINICAL(
        "Hipertiroidismo subclínico",
        "Los niveles de T3 y T4 en sangre son normales, pero el nivel de TSH está anormalmente bajo. Con frecuencia no causa síntomas evidentes."
    );

    fun getConditionType(): ConditionType {
        return when (this) {
            HYPOTHYROIDISM_PRIMARY, HYPOTHYROIDISM_SECONDARY, HYPOTHYROIDISM_TERTIARY,
            HYPOTHYROIDISM_SUBCLINICAL, HYPOTHYROIDISM_CONGENITAL -> ConditionType.HYPOTHYROIDISM
            HYPERTHYROIDISM_PRIMARY, HYPERTHYROIDISM_SECONDARY, HYPERTHYROIDISM_TERTIARY,
            HYPERTHYROIDISM_SUBCLINICAL -> ConditionType.HYPERTHYROIDISM
        }
    }

    companion object {
        fun getSubtypesForType(conditionType: ConditionType): List<ConditionSubtype> {
            return when (conditionType) {
                ConditionType.HYPOTHYROIDISM -> listOf(
                    HYPOTHYROIDISM_PRIMARY,
                    HYPOTHYROIDISM_SECONDARY,
                    HYPOTHYROIDISM_TERTIARY,
                    HYPOTHYROIDISM_SUBCLINICAL,
                    HYPOTHYROIDISM_CONGENITAL
                )
                ConditionType.HYPERTHYROIDISM -> listOf(
                    HYPERTHYROIDISM_PRIMARY,
                    HYPERTHYROIDISM_SECONDARY,
                    HYPERTHYROIDISM_TERTIARY,
                    HYPERTHYROIDISM_SUBCLINICAL
                )
                ConditionType.OTHER -> emptyList()
            }
        }
    }
}

@HiltViewModel
class OnboardingStep1ViewModel @Inject constructor() : ViewModel() {

    private val _profile = MutableLiveData<PatientProfile>(PatientProfile())
    val profile: LiveData<PatientProfile> = _profile

    private val _selectedConditionType = MutableLiveData<ConditionType?>(null)
    val selectedConditionType: LiveData<ConditionType?> = _selectedConditionType

    private val _selectedConditionSubtype = MutableLiveData<ConditionSubtype?>(null)
    val selectedConditionSubtype: LiveData<ConditionSubtype?> = _selectedConditionSubtype

    private val _availableSubtypes = MutableLiveData<List<ConditionSubtype>>(emptyList())
    val availableSubtypes: LiveData<List<ConditionSubtype>> = _availableSubtypes

    private val _isValid = MutableLiveData(false)
    val isValid: LiveData<Boolean> = _isValid

    private val _showSubtypeSelection = MutableLiveData(false)
    val showSubtypeSelection: LiveData<Boolean> = _showSubtypeSelection

    init {
        _selectedConditionType.value = ConditionType.HYPOTHYROIDISM
        _availableSubtypes.value = ConditionSubtype.getSubtypesForType(ConditionType.HYPOTHYROIDISM)
        _selectedConditionSubtype.value = _availableSubtypes.value.firstOrNull()
        _showSubtypeSelection.value = true
        updateProfile()
    }

    fun updateName(name: String) {
        val current = _profile.value ?: PatientProfile()
        _profile.value = current.copy(name = name.trim())
        validate()
    }

    fun updateBirthYear(year: Int) {
        val current = _profile.value ?: PatientProfile()
        _profile.value = current.copy(birthYear = year)
        validate()
    }

    fun selectConditionType(conditionType: ConditionType) {
        _selectedConditionType.value = conditionType
        _availableSubtypes.value = ConditionSubtype.getSubtypesForType(conditionType)
        if (_availableSubtypes.value.isNotEmpty()) {
            _selectedConditionSubtype.value = _availableSubtypes.value.first()
        } else {
            _selectedConditionSubtype.value = null
        }
        _showSubtypeSelection.value = conditionType != ConditionType.OTHER
        val current = _profile.value ?: PatientProfile()
        _profile.value = current.copy(conditionType = conditionType, conditionSubtype = _selectedConditionSubtype.value)
        validate()
    }

    fun selectConditionSubtype(subtype: ConditionSubtype) {
        _selectedConditionSubtype.value = subtype
        val current = _profile.value ?: PatientProfile()
        _profile.value = current.copy(conditionSubtype = subtype)
        validate()
    }

    fun togglePregnancy(checked: Boolean) {
        val current = _profile.value ?: PatientProfile()
        _profile.value = current.copy(hasPregnancy = checked)
    }

    fun toggleHeartProblems(checked: Boolean) {
        val current = _profile.value ?: PatientProfile()
        _profile.value = current.copy(hasHeartProblems = checked)
    }

    fun toggleOsteoporosis(checked: Boolean) {
        val current = _profile.value ?: PatientProfile()
        _profile.value = current.copy(hasOsteoporosis = checked)
    }

    private fun validate() {
        val profile = _profile.value
        _isValid.value = profile != null &&
            profile.name.isNotBlank() &&
            profile.birthYear != null &&
            profile.conditionType != null &&
            (profile.conditionSubtype != null || profile.conditionType == ConditionType.OTHER)
    }
}
