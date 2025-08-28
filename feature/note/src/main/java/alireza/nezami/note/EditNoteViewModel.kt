package alireza.nezami.note

import alireza.nezami.common.utils.base.BaseViewModel
import alireza.nezami.model.domain.Reminder
import alireza.nezami.note.presentation.contract.EditNoteEvent
import alireza.nezami.note.presentation.contract.EditNoteIntent
import alireza.nezami.note.presentation.contract.EditNoteUiState
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
        savedStateHandle: SavedStateHandle
) : BaseViewModel<EditNoteUiState, EditNoteUiState.EditNotePartialState, EditNoteEvent, EditNoteIntent>(
    savedStateHandle, EditNoteUiState()
) {

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
                    time = LocalDateTime.of(
                        uiState.value.selectedDate, uiState.value.selectedTime
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

            EditNoteIntent.SaveNote -> emptyFlow()
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
    }
}