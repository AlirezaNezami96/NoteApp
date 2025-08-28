package alireza.nezami.data.service

import alireza.nezami.model.domain.Note
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import javax.inject.Inject

interface AlarmScheduler {
    fun schedule(note: Note)
    fun cancel(note: Note)
    fun isAlarmSet(note: Note): Boolean
}

class AlarmSchedulerImpl @Inject constructor(
        private val context: Context
) : AlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(note: Note) {
        val reminder = note.reminder ?: return
        if (!reminder.isEnabled) return

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(EXTRA_NOTE_ID, note.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            note.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val reminderTimeMillis =
            reminder.time.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent
            )
        }
    }

    override fun cancel(note: Note) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(EXTRA_NOTE_ID, note.id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            note.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    override fun isAlarmSet(note: Note): Boolean {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(EXTRA_NOTE_ID, note.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            note.id.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        return pendingIntent != null
    }

    companion object {
        const val EXTRA_NOTE_ID = "EXTRA_NOTE_ID"
    }
}