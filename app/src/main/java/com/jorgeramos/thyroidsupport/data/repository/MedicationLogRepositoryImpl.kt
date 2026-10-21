package com.jorgeramos.thyroidsupport.data.repository

import com.jorgeramos.thyroidsupport.data.MedicationLogDao
import com.jorgeramos.thyroidsupport.domain.MedicationLog
import com.jorgeramos.thyroidsupport.domain.repository.MedicationLogRepository
import javax.inject.Inject

class MedicationLogRepositoryImpl @Inject constructor(
    private val medicationLogDao: MedicationLogDao
) : MedicationLogRepository {

    override suspend fun insert(log: MedicationLog): Long =
        medicationLogDao.insert(log)

    override suspend fun getLogsForMedication(medicationId: Long): List<MedicationLog> =
        medicationLogDao.getLogsForMedication(medicationId)

    override suspend fun getLogsForMedicationRange(
        medicationId: Long,
        startDate: Long,
        endDate: Long
    ): List<MedicationLog> =
        medicationLogDao.getLogsForMedicationRange(medicationId, startDate, endDate)

    override suspend fun getLogForDate(medicationId: Long, date: Long): MedicationLog? =
        medicationLogDao.getLogForDate(medicationId, date)

    override suspend fun delete(id: Long) =
        medicationLogDao.delete(id)
}
