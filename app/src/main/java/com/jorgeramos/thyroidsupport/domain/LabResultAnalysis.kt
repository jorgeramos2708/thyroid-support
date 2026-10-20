package com.jorgeramos.thyroidsupport.domain

data class LabResultAnalysis(
    val tshStatus: ThyroidStatus,
    val ft4Status: ThyroidStatus?,
    val ft3Status: ThyroidStatus?,
    val tpoAbStatus: AntibodyStatus?,
    val overallInterpretation: String,
    val confidence: Double // 0.0 - 1.0
)

enum class ThyroidStatus {
    NORMAL,
    LOW,      // Below reference range
    HIGH,     // Above reference range
    BORDERLINE_LOW,
    BORDERLINE_HIGH,
    UNKNOWN
}

enum class AntibodyStatus {
    NEGATIVE,
    POSITIVE,
    ELEVATED,
    UNKNOWN
}
