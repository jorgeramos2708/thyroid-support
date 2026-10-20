package com.jorgeramos.thyroidsupport.data.repository

import android.content.Context
import com.jorgeramos.thyroidsupport.data.AppDatabase
import com.jorgeramos.thyroidsupport.domain.repository.ExportRepository
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

class ExportRepositoryImpl @Inject constructor(
    private val context: Context,
    private val database: AppDatabase
) : ExportRepository {

    private val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())

    override suspend fun exportSymptomsToCsv(patientId: Long): File {
        val symptoms = database.symptomDao().getSymptomsForPatient(patientId)
        return exportToCsv(
            filename = "symptoms_${dateFormat.format(Date())}.csv",
            header = "id,recorded_at,type,severity,notes",
            rows = symptoms.map { s ->
                "${s.id},${s.recordedAt},${s.type},${s.severity},${s.notes.orEmpty()}"
            }
        )
    }

    override suspend fun exportLabsToCsv(patientId: Long): File {
        val labs = database.labDao().getLabsForPatient(patientId)
        return exportToCsv(
            filename = "labs_${dateFormat.format(Date())}.csv",
            header = "id,test_date,test_type,value,reference_low,reference_high",
            rows = labs.map { l ->
                "${l.id},${l.testDate},${l.testType},${l.value},${l.referenceLow},${l.referenceHigh}"
            }
        )
    }

    override suspend fun exportMedicationsToCsv(patientId: Long): File {
        val meds = database.medicationDao().getMedicationsForPatient(patientId)
        return exportToCsv(
            filename = "medications_${dateFormat.format(Date())}.csv",
            header = "id,name,dose,frequency,start_date,end_date",
            rows = meds.map { m ->
                "${m.id},${m.name},${m.dose},${m.frequency},${m.startDate},${m.endDate.orEmpty()}"
            }
        )
    }

    override suspend fun exportSupplementsToCsv(patientId: Long): File {
        val sups = database.supplementDao().getSupplementsForPatient(patientId)
        return exportToCsv(
            filename = "supplements_${dateFormat.format(Date())}.csv",
            header = "id,name,dose,start_date,end_date",
            rows = sups.map { s ->
                "${s.id},${s.name},${s.dose},${s.startDate},${s.endDate.orEmpty()}"
            }
        )
    }

    override suspend fun exportAllDataToZip(patientId: Long): File {
        val timestamp = dateFormat.format(Date())
        val zipFile = File(context.externalCacheDir, "thyroid_support_export_$timestamp.zip")
        zipFile.outputStream().use { fos ->
            ZipOutputStream(fos).use { zos ->
                val files = mutableListOf<File>()
                files.add(exportSymptomsToCsv(patientId))
                files.add(exportLabsToCsv(patientId))
                files.add(exportMedicationsToCsv(patientId))
                files.add(exportSupplementsToCsv(patientId))

                files.forEach { csvFile ->
                    zos.putNextEntry(ZipEntry(csvFile.name))
                    csvFile.inputStream().use { zos.write(it.readBytes()) }
                    zos.closeEntry()
                }
            }
        }
        return zipFile
    }

    private fun exportToCsv(filename: String, header: String, rows: List<String>): File {
        val file = File(context.externalCacheDir, filename)
        file.writer().use { writer ->
            writer.write(header)
            writer.write("\n")
            rows.forEach { row ->
                writer.write(row)
                writer.write("\n")
            }
        }
        return file
    }
}
