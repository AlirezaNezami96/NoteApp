package alireza.nezami.data.service

import alireza.nezami.data.R
import alireza.nezami.model.domain.Note
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

object NotificationHelper {
    private const val CHANNEL_ID = "note_reminder_channel"
    private const val CHANNEL_NAME = "Note Reminders"

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for note reminder notifications"
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun createNotification(context: Context, note: Note) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification =
            NotificationCompat.Builder(context, CHANNEL_ID).setContentTitle(note.title)
                .setSmallIcon(alireza.nezami.designsystem.R.drawable.ic_avatar)
                .setContentText(note.content).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true).build()

        notificationManager.notify(note.id.toInt(), notification)
    }
}