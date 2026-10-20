package com.jorgeramos.thyroidsupport.presentation

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationScheduler @Inject constructor(
    private val context: Context,
    private val workManager: WorkManager
) {

    fun scheduleMedicationReminder(hour: Int, minute: Int) {
        val workRequest = PeriodicWorkRequest.Builder(
            MedicationReminderWorker::class.java,
            24, TimeUnit.HOURS
        ).build()

        workManager.enqueueUniquePeriodicWork(
            "medication_reminder",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelMedicationReminder() {
        workManager.cancelUniqueWork("medication_reminder")
    }
}
