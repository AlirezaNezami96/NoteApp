package alireza.nezami.note.presentation.contract

import alireza.nezami.common.extensions.plusHours
import alireza.nezami.common.extensions.plusMinutes
import alireza.nezami.model.domain.Note
import alireza.nezami.model.domain.Reminder
import alireza.nezami.model.domain.RepeatInterval
import android.os.Parcelable
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
data class EditNoteUiState(
        val title: String = "",
        val content: String = "",
        val labels: List<String> = emptyList(),
        val reminder: Reminder? = null,
        val isLabelDialogVisible: Boolean = false,
        val isReminderBottomSheetVisible: Boolean = false,
        val isReminderDialogVisible: Boolean = false,
        val selectedDate: LocalDate = LocalDate.now(),
        val selectedTime: LocalTime = LocalTime.now().plusHours(1),
        val selectedRepeatInterval: RepeatInterval = RepeatInterval.NONE,
        val suggestedReminders: @RawValue List<LocalDateTime> = run {
            val zone = TimeZone.currentSystemDefault()
            val now = Clock.System.now().toLocalDateTime(zone)
            listOf(
                now.plusMinutes(10, zone), now.plusHours(24, zone)
            )
        }
) : Parcelable {
    sealed class EditNotePartialState {
        data class TitleChanged(val title: String) : EditNotePartialState()
        data class ContentChanged(val content: String) : EditNotePartialState()
        data class LabelAdded(val label: String) : EditNotePartialState()
        data class LabelRemoved(val label: String) : EditNotePartialState()
        data class ReminderSet(val reminder: Reminder) : EditNotePartialState()
        data class ReminderRemoved(val reminderId: Long) : EditNotePartialState()
        data class ShowLabelDialog(val show: Boolean) : EditNotePartialState()
        data class ShowReminderBottomSheet(val show: Boolean) : EditNotePartialState()
        data class ShowReminderDialog(val show: Boolean) : EditNotePartialState()
        data class DateSelected(val date: LocalDate) : EditNotePartialState()
        data class TimeSelected(val time: LocalTime) : EditNotePartialState()
        data class RepeatIntervalSelected(val interval: RepeatInterval) : EditNotePartialState()
        data class NoteUpdated( val note: Note) : EditNotePartialState()
    }
}