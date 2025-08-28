package alireza.nezami.note.presentation.contract

import java.time.LocalDate
import java.time.LocalTime

sealed class EditNoteEvent {
    data object NavigateBack : EditNoteEvent()
    data class ShowSuccess(val message: String) : EditNoteEvent()
}