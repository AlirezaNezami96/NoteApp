package alireza.nezami.note

import alireza.nezami.common.base.BaseViewModel
import alireza.nezami.data.service.AlarmScheduler
import alireza.nezami.domain.usecase.InsertNoteUseCase
import alireza.nezami.domain.usecase.UpdateNoteUseCase
import alireza.nezami.model.domain.Note
import alireza.nezami.model.domain.Reminder
import alireza.nezami.note.navigation.DetailArgs
import alireza.nezami.note.presentation.contract.EditNoteEvent
import alireza.nezami.note.presentation.contract.EditNoteIntent
import alireza.nezami.note.presentation.contract.EditNoteUiState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
        savedStateHandle: SavedStateHandle,
        private val insertNoteUseCase: InsertNoteUseCase,
        private val updateNoteUseCase: UpdateNoteUseCase,
        private val alarmScheduler: AlarmScheduler
) : BaseViewModel<EditNoteUiState, EditNoteUiState.EditNotePartialState, EditNoteEvent, EditNoteIntent>(
    savedStateHandle, EditNoteUiState()
) {
    private val note: Note? = DetailArgs.fromSavedStateHandle(savedStateHandle)?.note

    init {
        if (note != null) {
            acceptIntent(EditNoteIntent.SaveNote(note))
        }
    }

    override fun mapIntents(intent: EditNoteIntent): Flow<EditNoteUiState.EditNotePartialState> =
        when (intent) {
            is EditNoteIntent.UpdateTitle -> flow {
                emit(EditNoteUiState.EditNotePartialState.TitleChanged(intent.title))
            }

            is EditNoteIntent.UpdateContent -> flow {
                emit(EditNoteUiState.EditNotePartialState.ContentChanged(intent.content))
            }

            is EditNoteIntent.AddLabel -> flow {
                emit(EditNoteUiState.EditNotePartialState.LabelAdded(intent.label))
                emit(EditNoteUiState.EditNotePartialState.ShowLabelDialog(false))
            }

            is EditNoteIntent.RemoveLabel -> flow {
                emit(EditNoteUiState.EditNotePartialState.LabelRemoved(intent.label))
            }

            EditNoteIntent.ToggleLabelDialog -> flow {
                emit(EditNoteUiState.EditNotePartialState.ShowLabelDialog(!uiState.value.isLabelDialogVisible))
            }

            EditNoteIntent.ToggleReminderBottomSheet -> flow {
                emit(EditNoteUiState.EditNotePartialState.ShowReminderBottomSheet(!uiState.value.isReminderBottomSheetVisible))
            }

            EditNoteIntent.ToggleReminderDialog -> flow {
                emit(EditNoteUiState.EditNotePartialState.ShowReminderDialog(!uiState.value.isReminderDialogVisible))
            }

            is EditNoteIntent.SelectSuggestedReminder -> flow {
                emit(
                    EditNoteUiState.EditNotePartialState.ReminderSet(
                        Reminder(
                            time = intent.dateTime, isEnabled = true
                        )
                    )
                )
                emit(EditNoteUiState.EditNotePartialState.ShowReminderBottomSheet(false))
            }

            EditNoteIntent.SaveReminder -> flow {
                val reminder = Reminder(
                    time = LocalDateTime(
                        year = uiState.value.selectedDate.year,
                        monthNumber = uiState.value.selectedDate.monthValue,
                        dayOfMonth = uiState.value.selectedDate.dayOfMonth,
                        hour = uiState.value.selectedTime.hour,
                        minute = uiState.value.selectedTime.minute,
                        second = uiState.value.selectedTime.second
                    ), isEnabled = true, repeatInterval = uiState.value.selectedRepeatInterval
                )

                emit(EditNoteUiState.EditNotePartialState.ReminderSet(reminder))
                emit(EditNoteUiState.EditNotePartialState.ShowReminderDialog(false))
            }

            is EditNoteIntent.SelectDate -> flow {
                emit(EditNoteUiState.EditNotePartialState.DateSelected(intent.date))
            }

            is EditNoteIntent.SelectTime -> flow {
                emit(EditNoteUiState.EditNotePartialState.TimeSelected(intent.time))
            }

            is EditNoteIntent.SelectRepeatInterval -> flow {
                emit(EditNoteUiState.EditNotePartialState.RepeatIntervalSelected(intent.interval))
            }

            EditNoteIntent.RemoveReminder -> flow {
                emit(EditNoteUiState.EditNotePartialState.ReminderRemoved(0L))
            }

            is EditNoteIntent.SaveNote -> flow {
                saveNoteWithAlarm(intent.note)

                val note = intent.note
                if (note != null) {
                    emit(EditNoteUiState.EditNotePartialState.NoteUpdated(note))
                }
            }
        }

    override fun reduceUiState(
            previousState: EditNoteUiState, partialState: EditNoteUiState.EditNotePartialState
    ): EditNoteUiState = when (partialState) {
        is EditNoteUiState.EditNotePartialState.TitleChanged -> previousState.copy(title = partialState.title)
        is EditNoteUiState.EditNotePartialState.ContentChanged -> previousState.copy(content = partialState.content)
        is EditNoteUiState.EditNotePartialState.LabelAdded -> previousState.copy(labels = previousState.labels + partialState.label)
        is EditNoteUiState.EditNotePartialState.LabelRemoved -> previousState.copy(labels = previousState.labels - partialState.label)
        is EditNoteUiState.EditNotePartialState.ReminderSet -> previousState.copy(reminder = partialState.reminder)
        is EditNoteUiState.EditNotePartialState.ReminderRemoved -> previousState.copy(reminder = null)
        is EditNoteUiState.EditNotePartialState.ShowLabelDialog -> previousState.copy(
            isLabelDialogVisible = partialState.show
        )

        is EditNoteUiState.EditNotePartialState.ShowReminderBottomSheet -> previousState.copy(
            isReminderBottomSheetVisible = partialState.show
        )

        is EditNoteUiState.EditNotePartialState.ShowReminderDialog -> previousState.copy(
            isReminderDialogVisible = partialState.show
        )

        is EditNoteUiState.EditNotePartialState.DateSelected -> previousState.copy(selectedDate = partialState.date)
        is EditNoteUiState.EditNotePartialState.TimeSelected -> previousState.copy(selectedTime = partialState.time)
        is EditNoteUiState.EditNotePartialState.RepeatIntervalSelected -> previousState.copy(
            selectedRepeatInterval = partialState.interval
        )

        is EditNoteUiState.EditNotePartialState.NoteUpdated -> previousState.copy(
            title = partialState.note.title,
            content = partialState.note.content,
            reminder = partialState.note.reminder,
            labels = partialState.note.labels
        )
    }

    private fun saveNoteWithAlarm(noteToSave: Note?) {
        viewModelScope.launch {
            try {
                val currentState = uiState.value
                val noteData = noteToSave ?: createNoteFromCurrentState(currentState)

                val savedNoteId = if (note == null) {
                    insertNoteUseCase(noteData)
                } else {
                    val updatedNote = noteData.copy(id = note.id)
                    updateNoteUseCase(updatedNote)
                    note.id
                }

                val savedNote = noteData.copy(id = savedNoteId)
                Timber.d("Saved Note $savedNote")

                if (savedNote.reminder?.isEnabled == true) {
                    alarmScheduler.schedule(savedNote)
                }

                val isAlarmSet = alarmScheduler.isAlarmSet(savedNote)
                Timber.d("Alarm is set: $isAlarmSet")

            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }


    private fun createNoteFromCurrentState(state: EditNoteUiState): Note {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return Note(
            id = 0L,
            title = state.title,
            content = state.content,
            createdAt = now,
            updatedAt = now,
            labels = state.labels,
            reminder = state.reminder
        )
    }
}