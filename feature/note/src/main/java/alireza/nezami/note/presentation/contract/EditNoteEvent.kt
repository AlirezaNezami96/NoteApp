package alireza.nezami.note.presentation.contract

import java.time.LocalDate
import java.time.LocalTime

sealed class EditNoteEvent {
    data class ShowDatePicker(val initialDate: LocalDate) : EditNoteEvent()
    data class ShowTimePicker(val initialTime: LocalTime) : EditNoteEvent()
    data object NavigateBack : EditNoteEvent()
    data class ShowError(val message: String) : EditNoteEvent()
    data class ShowSuccess(val message: String) : EditNoteEvent()
}