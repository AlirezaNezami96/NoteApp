package alireza.nezami.note.presentation.contract

import alireza.nezami.model.domain.RepeatInterval
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

sealed class EditNoteIntent {
    data class UpdateTitle(val title: String) : EditNoteIntent()
    data class UpdateContent(val content: String) : EditNoteIntent()
    data object ToggleLabelDialog : EditNoteIntent()
    data class AddLabel(val label: String) : EditNoteIntent()
    data class RemoveLabel(val label: String) : EditNoteIntent()
    data object ToggleReminderBottomSheet : EditNoteIntent()
    data object ToggleReminderDialog : EditNoteIntent()
    data class SelectSuggestedReminder(val dateTime: LocalDateTime) : EditNoteIntent()
    data class SelectDate(val date: LocalDate) : EditNoteIntent()
    data class SelectTime(val time: LocalTime) : EditNoteIntent()
    data class SelectRepeatInterval(val interval: RepeatInterval) : EditNoteIntent()
    data object SaveReminder : EditNoteIntent()
    data object RemoveReminder : EditNoteIntent()
    data object SaveNote : EditNoteIntent()
}