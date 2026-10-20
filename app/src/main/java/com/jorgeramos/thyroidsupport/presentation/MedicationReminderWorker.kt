package com.jorgeramos.thyroidsupport.presentation

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.ListenableWorker
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat
import com.jorgeramos.thyroidsupport.R
import com.jorgeramos.thyroidsupport.domain.repository.MedicationRepository
import kotlinx.coroutines.runBlocking
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import androidx.hilt.work.HiltWorker

@HiltWorker
class MedicationReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val medicationRepository: MedicationRepository
) : Worker(context, workerParams) {

    override fun doWork(): ListenableWorker.Result {
        val channelId = "medication_reminder"
        val notificationManager = NotificationManagerCompat.from(applicationContext)

        // Crear canal de notificación (Android 8+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                channelId,
                "Recordatorios de Medicación",
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Recordatorios para tomar tu medicación tiroidea"
                enableLights(true)
                lightColor = android.graphics.Color.BLUE
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Recordatorio de medicación")
            .setContentText("Es hora de tomar tu medicación tiroidea. Recuerda tomarla en ayunas.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)

        return ListenableWorker.Result.success()
    }
}
