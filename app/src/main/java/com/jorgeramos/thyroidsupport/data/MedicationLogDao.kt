package com.jorgeramos.thyroidsupport.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jorgeramos.thyroidsupport.domain.MedicationLog

@Dao
interface MedicationLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: MedicationLog): Long

    @Query("SELECT * FROM medication_log WHERE medicationId = :medicationId ORDER BY date DESC")
    suspend fun getLogsForMedication(medicationId: Long): List<MedicationLog>

    @Query("SELECT * FROM medication_log WHERE medicationId = :medicationId AND date >= :startDate AND date <= :endDate ORDER BY date DESC")
    suspend fun getLogsForMedicationRange(
        medicationId: Long,
        startDate: Long,
        endDate: Long
    ): List<MedicationLog>

    @Query("SELECT * FROM medication_log WHERE medicationId = :medicationId AND date = :date")
    suspend fun getLogForDate(medicationId: Long, date: Long): MedicationLog?

    @Query("DELETE FROM medication_log WHERE id = :id")
    suspend fun delete(id: Long)
}
