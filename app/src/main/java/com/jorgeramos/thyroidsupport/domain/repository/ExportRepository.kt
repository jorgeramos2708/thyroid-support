package com.jorgeramos.thyroidsupport.domain.repository

import java.io.File

interface ExportRepository {

    suspend fun exportSymptomsToCsv(patientId: Long): File
    suspend fun exportLabsToCsv(patientId: Long): File
    suspend fun exportMedicationsToCsv(patientId: Long): File
    suspend fun exportSupplementsToCsv(patientId: Long): File
    suspend fun exportAllDataToZip(patientId: Long): File
}
