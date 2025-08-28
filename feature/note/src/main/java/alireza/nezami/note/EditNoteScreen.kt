package alireza.nezami.note

import alireza.nezami.common.extensions.toDetailedReminderString
import alireza.nezami.common.extensions.toReminderString
import alireza.nezami.designsystem.R
import alireza.nezami.designsystem.components.HomeTopBar
import alireza.nezami.designsystem.components.TopBarStyle
import alireza.nezami.model.domain.RepeatInterval
import alireza.nezami.note.presentation.contract.EditNoteIntent
import alireza.nezami.note.presentation.contract.EditNoteUiState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDate
import kotlinx.datetime.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun EditNoteScreen(
        viewModel: EditNoteViewModel = hiltViewModel(), onBackPress: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    EditNoteContent(
        uiState = uiState, onIntent = viewModel::acceptIntent, onBackPress = onBackPress
    )

    // Label Dialog
    if (uiState.isLabelDialogVisible) {
        AddLabelDialog(
            onDismiss = { viewModel.acceptIntent(EditNoteIntent.ToggleLabelDialog) },
            onConfirm = { label -> viewModel.acceptIntent(EditNoteIntent.AddLabel(label)) })
    }

    // Reminder Bottom Sheet
    if (uiState.isReminderBottomSheetVisible) {
        ReminderBottomSheet(
            suggestedReminders = uiState.suggestedReminders,
            onDismiss = { viewModel.acceptIntent(EditNoteIntent.ToggleReminderBottomSheet) },
            onTimeSelected = { dateTime ->
                viewModel.acceptIntent(EditNoteIntent.SelectSuggestedReminder(dateTime))
            },
            onPickDateClick = { viewModel.acceptIntent(EditNoteIntent.ToggleReminderDialog) })
    }

    // Reminder Dialog
    if (uiState.isReminderDialogVisible) {
        ReminderDialog(
            selectedDate = uiState.selectedDate,
            selectedTime = uiState.selectedTime,
            selectedRepeatInterval = uiState.selectedRepeatInterval,
            onDateSelected = { date -> viewModel.acceptIntent(EditNoteIntent.SelectDate(date)) },
            onTimeSelected = { time -> viewModel.acceptIntent(EditNoteIntent.SelectTime(time)) },
            onRepeatIntervalSelected = { interval ->
                viewModel.acceptIntent(EditNoteIntent.SelectRepeatInterval(interval))
            },
            onDismiss = { viewModel.acceptIntent(EditNoteIntent.ToggleReminderDialog) },
            onSave = { viewModel.acceptIntent(EditNoteIntent.SaveReminder) })
    }
}

@Composable
private fun EditNoteContent(
        uiState: EditNoteUiState, onIntent: (EditNoteIntent) -> Unit, onBackPress: () -> Unit
) {
    Scaffold(topBar = {
        HomeTopBar(
            style = TopBarStyle.Edit,
            onBack = onBackPress,
            addReminder = { onIntent(EditNoteIntent.ToggleReminderBottomSheet) },
        )
    }, bottomBar = {
        EditNoteBottomBar(
            onLabelsClick = { onIntent(EditNoteIntent.ToggleLabelDialog) },
            onAddClick = { onIntent(EditNoteIntent.ToggleReminderDialog) })
    }) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(16.dp)
        ) {

            Row(modifier = Modifier.fillMaxWidth()) {

                uiState.reminder?.let { reminder ->
                    AssistChip(
                        colors = AssistChipDefaults.assistChipColors().copy(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                        onClick = { },
                        label = { Text(reminder.time.toDetailedReminderString()) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_timer),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surface
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { onIntent(EditNoteIntent.RemoveReminder) }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove reminder",
                                    tint = MaterialTheme.colorScheme.surface
                                )
                            }
                        })
                }

                Spacer(modifier = Modifier.width(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.labels) { label ->
                        AssistChip(onClick = { }, label = { Text(label) }, trailingIcon = {
                            IconButton(
                                onClick = { onIntent(EditNoteIntent.RemoveLabel(label)) }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove label"
                                )
                            }
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = uiState.title,
                onValueChange = { onIntent(EditNoteIntent.UpdateTitle(it)) },
                placeholder = { Text(stringResource(R.string.title)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))


            TextField(
                value = uiState.content,
                onValueChange = { onIntent(EditNoteIntent.UpdateContent(it)) },
                placeholder = { Text("Note") },
                modifier = Modifier.fillMaxWidth().weight(1f),
                textStyle = MaterialTheme.typography.bodyLarge
            )

        }
    }
}

@Composable
fun AddLabelDialog(
        onDismiss: () -> Unit, onConfirm: (String) -> Unit
) {
    var label by remember { mutableStateOf("") }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Add Label") }, text = {
        TextField(
            value = label,
            onValueChange = { label = it },
            placeholder = { Text("Enter label") },
            singleLine = true
        )
    }, confirmButton = {
        TextButton(
            onClick = {
                if (label.isNotBlank()) {
                    onConfirm(label)
                }
            }) {
            Text("Save")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderBottomSheet(
        suggestedReminders: List<LocalDateTime>,
        onDismiss: () -> Unit,
        onTimeSelected: (LocalDateTime) -> Unit,
        onPickDateClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            suggestedReminders.forEach { dateTime ->
                SuggestionItem(
                    dateTime = dateTime, onClick = { onTimeSelected(dateTime) })
                Spacer(modifier = Modifier.height(8.dp))
            }

            TextButton(
                onClick = onPickDateClick, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pick a date")
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SuggestionItem(
        dateTime: LocalDateTime, onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            dateTime.toReminderString(),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDialog(
        selectedDate: LocalDate,
        selectedTime: LocalTime,
        selectedRepeatInterval: RepeatInterval,
        onDateSelected: (LocalDate) -> Unit,
        onTimeSelected: (LocalTime) -> Unit,
        onRepeatIntervalSelected: (RepeatInterval) -> Unit,
        onDismiss: () -> Unit,
        onSave: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = selectedTime.hour,
        initialMinute = selectedTime.minute
    )

    // Main Dialog Content
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Reminder") },
        text = {
            Column {
                // Date Selection
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Time Selection
                OutlinedButton(
                    onClick = { showTimePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a")))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Repeat Interval
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = { }
                ) {
                    OutlinedTextField(
                        value = selectedRepeatInterval.name,
                        onValueChange = { },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                        modifier = Modifier
                    )

                    ExposedDropdownMenu(
                        expanded = false,
                        onDismissRequest = { }
                    ) {
                        RepeatInterval.entries.forEach { interval ->
                            DropdownMenuItem(
                                text = { Text(interval.name) },
                                onClick = { onRepeatIntervalSelected(interval) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = selectedDate.atStartOfDay()
                        .toInstant(ZoneOffset.UTC)
                        .toEpochMilli()
                ),
                showModeToggle = false
            )
        }
    }

    // Time Picker Dialog
    if (showTimePicker) {
        AlertDialog(
            title = { Text("Pick time") },
            text = {
                TimeInput(
                    state = rememberTimePickerState(
                        initialHour = selectedTime.hour,
                        initialMinute = selectedTime.minute
                    )
                )
            },
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onTimeSelected(
                            LocalTime.of(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                        )
                        showTimePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showTimePicker = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun EditNoteBottomBar(
        onLabelsClick: () -> Unit, onAddClick: () -> Unit, modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onLabelsClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tag),
                    contentDescription = "Labels",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Labels",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}
