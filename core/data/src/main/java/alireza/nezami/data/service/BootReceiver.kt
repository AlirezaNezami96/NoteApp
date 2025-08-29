package alireza.nezami.data.service

import alireza.nezami.domain.repository.NoteRepository
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class BootReceiver @Inject constructor(
    private val noteRepository: NoteRepository,
    private val alarmScheduler: AlarmScheduler
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            CoroutineScope(Dispatchers.IO).launch {
                val notesWithReminders = noteRepository
                    .getUpcomingReminders(System.currentTimeMillis())
                    .first()

                notesWithReminders.forEach { note ->
                    alarmScheduler.schedule(note)
                }
            }
        }
    }
}