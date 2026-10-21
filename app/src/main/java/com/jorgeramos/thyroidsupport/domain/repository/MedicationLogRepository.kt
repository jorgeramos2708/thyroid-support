package com.jorgeramos.thyroidsupport.domain.repository

import com.jorgeramos.thyroidsupport.domain.MedicationLog

interface MedicationLogRepository {

    suspend fun insert(log: MedicationLog): Long
    suspend fun getLogsForMedication(medicationId: Long): List<MedicationLog>
    suspend fun getLogsForMedicationRange(
        medicationId: Long,
        startDate: Long,
        endDate: Long
    ): List<MedicationLog>
    suspend fun getLogForDate(medicationId: Long, date: Long): MedicationLog?
    suspend fun delete(id: Long)
}
