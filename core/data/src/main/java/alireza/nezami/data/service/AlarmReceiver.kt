package alireza.nezami.data.service

import alireza.nezami.common.extensions.plusDays
import alireza.nezami.common.extensions.plusMonths
import alireza.nezami.common.extensions.plusWeeks
import alireza.nezami.common.extensions.plusYears
import alireza.nezami.domain.repository.NoteRepository
import alireza.nezami.model.domain.RepeatInterval
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AlarmReceiverEntryPoint {
    fun getNoteRepository(): NoteRepository
    fun getAlarmScheduler(): AlarmScheduler
}


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            AlarmReceiverEntryPoint::class.java
        )
        val noteRepository: NoteRepository = entryPoint.getNoteRepository()
        val alarmScheduler: AlarmScheduler = entryPoint.getAlarmScheduler()

        val noteId = intent.getLongExtra(AlarmSchedulerImpl.EXTRA_NOTE_ID, -1L)
        if (noteId == -1L) return

        CoroutineScope(Dispatchers.IO).launch {
            val note = noteRepository.getNoteById(noteId) ?: return@launch

            NotificationHelper.createNotification(context, note)

            val reminder = note.reminder ?: return@launch
            if (reminder.repeatInterval == RepeatInterval.NONE) {
                noteRepository.clearReminder(noteId)
            } else {
                val nextReminderTime =
                    calculateNextReminderTime(reminder.time, reminder.repeatInterval)
                noteRepository.updateReminderTime(noteId, nextReminderTime)

                val updatedNote = noteRepository.getNoteById(noteId) ?: return@launch
                alarmScheduler.schedule(updatedNote)
            }
        }
    }

    private fun calculateNextReminderTime(
            currentTime: LocalDateTime,
            interval: RepeatInterval
    ): LocalDateTime {
        return when (interval) {
            RepeatInterval.DAILY -> currentTime.plusDays(1)
            RepeatInterval.WEEKLY -> currentTime.plusWeeks(1)
            RepeatInterval.BIWEEKLY -> currentTime.plusWeeks(2)
            RepeatInterval.MONTHLY -> currentTime.plusMonths(1)
            RepeatInterval.QUARTERLY -> currentTime.plusMonths(3)
            RepeatInterval.BIANNUALLY -> currentTime.plusMonths(6)
            RepeatInterval.YEARLY -> currentTime.plusYears(1)
            RepeatInterval.WEEKDAYS -> {
                var next = currentTime.plusDays(1)
                while (next.dayOfWeek.value > 5) {
                    next = next.plusDays(1)
                }
                next
            }

            RepeatInterval.WEEKENDS -> {
                var next = currentTime.plusDays(1)
                while (next.dayOfWeek.value < 6) {
                    next = next.plusDays(1)
                }
                next
            }

            RepeatInterval.NONE -> currentTime
        }
    }
}