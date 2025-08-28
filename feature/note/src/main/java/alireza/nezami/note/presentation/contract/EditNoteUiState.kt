package alireza.nezami.note.presentation.contract

import alireza.nezami.model.domain.Reminder
import alireza.nezami.model.domain.RepeatInterval
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime
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
        val suggestedReminders: List<LocalDateTime> = listOf(
        LocalDateTime.now().plusHours(12),
        LocalDateTime.now().plusHours(24)
    )
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
    }
}