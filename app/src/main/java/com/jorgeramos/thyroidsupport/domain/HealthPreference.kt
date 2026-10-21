package com.jorgeramos.thyroidsupport.domain

data class HealthPreference(
    val flags: Set<String> = emptySet(),
    val otherNote: String = ""
)

data class HealthGroup(
    val id: String,
    val title: String,
    val options: List<HealthOption>
)

data class HealthOption(
    val id: String,
    val label: String
)

object HealthGroups {
    // Claves para las opciones
    const val EMBARAZO_PLANIFICACION = "embarazo_planificacion"
    const val ENFERMEDAD_CORAZON_ARRITMIAS = "enfermedad_corazon_arritmias"
    const val OSTEOPOROSIS = "osteoporosis"
    const val REFLUJO_GASTRITIS = "reflujo_gastritis"
    const val CELIAQUIA = "celiaquia"
    const val ENFERMEDAD_INFLAMATORIA_INTESTINAL = "enfermedad_inflamatoria_intestinal"
    const val INTOLERANCIA_LACTOSA = "intolerancia_lactosa"
    const val CIRUGIA_GASTRICA_INTESTINAL = "cirugia_gastrica_intestinal"
    const val ANTICONCEPTIVOS_TERAPIA_HORMONAL = "anticonceptivos_terapia_hormonal"
    const val TIROIDES_EXTIRPADA = "tiroides_extirpada"
    const val YODO_RADIACTIVO = "yodo_radiactivo"
    const val CANCER_TIROIDES = "cancer_tiroides"
    const val DIABETES_TIPO_1 = "diabetes_tipo_1"
    const val ENFERMEDAD_ADDISON = "enfermedad_addison"
    const val ANEMIA_PERNICIOSA = "anemia_perniciosa"
    const val NINGUNA = "ninguna"
    const val NO_LO_SE = "no_lo_se"

    val allGroups: List<HealthGroup> = listOf(
        HealthGroup(
            id = "general",
            title = "General",
            options = listOf(
                HealthOption(EMBARAZO_PLANIFICACION, "Embarazo o planificación"),
                HealthOption(ENFERMEDAD_CORAZON_ARRITMIAS, "Enfermedad del corazón o arritmias"),
                HealthOption(OSTEOPOROSIS, "Osteoporosis")
            )
        ),
        HealthGroup(
            id = "digestivo",
            title = "Digestivo",
            options = listOf(
                HealthOption(REFLUJO_GASTRITIS, "Reflujo o gastritis"),
                HealthOption(CELIAQUIA, "Celiaquía"),
                HealthOption(ENFERMEDAD_INFLAMATORIA_INTESTINAL, "Enfermedad inflamatoria intestinal"),
                HealthOption(INTOLERANCIA_LACTOSA, "Intolerancia a la lactosa"),
                HealthOption(CIRUGIA_GASTRICA_INTESTINAL, "Cirugía gástrica o intestinal")
            )
        ),
        HealthGroup(
            id = "hormonal",
            title = "Hormonal",
            options = listOf(
                HealthOption(ANTICONCEPTIVOS_TERAPIA_HORMONAL, "Anticonceptivos o terapia hormonal")
            )
        ),
        HealthGroup(
            id = "tiroides_cirugias",
            title = "Tiroides y cirugías",
            options = listOf(
                HealthOption(TIROIDES_EXTIRPADA, "Tiroides extirpada"),
                HealthOption(YODO_RADIACTIVO, "Yodo radiactivo"),
                HealthOption(CANCER_TIROIDES, "Cáncer de tiroides")
            )
        ),
        HealthGroup(
            id = "otras_autoimmune",
            title = "Otras enfermedades autoinmunes",
            options = listOf(
                HealthOption(DIABETES_TIPO_1, "Diabetes tipo 1"),
                HealthOption(ENFERMEDAD_ADDISON, "Enfermedad de Addison"),
                HealthOption(ANEMIA_PERNICIOSA, "Anemia perniciosa")
            )
        )
    )
}
