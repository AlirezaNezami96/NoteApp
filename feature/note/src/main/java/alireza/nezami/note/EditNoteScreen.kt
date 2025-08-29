package alireza.nezami.note

import alireza.nezami.common.extensions.toReminderString
import alireza.nezami.designsystem.R
import alireza.nezami.designsystem.components.HomeTopBar
import alireza.nezami.designsystem.components.LabelChip
import alireza.nezami.designsystem.components.ReminderChip
import alireza.nezami.designsystem.components.TopBarStyle
import alireza.nezami.model.domain.RepeatInterval
import alireza.nezami.note.presentation.contract.EditNoteEvent
import alireza.nezami.note.presentation.contract.EditNoteIntent
import alireza.nezami.note.presentation.contract.EditNoteUiState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun EditNoteScreen(
        viewModel: EditNoteViewModel = hiltViewModel(), onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val eventFlow = viewModel.event
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        eventFlow.collectLatest { event ->
            when (event) {
                is EditNoteEvent.ShowSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = event.message, duration = SnackbarDuration.Short
                    )
                }

                EditNoteEvent.NavigateBack -> onBack()
            }
        }
    }

    EditNoteContent(
        uiState = uiState,
        onIntent = viewModel::acceptIntent,
        onBackPress = onBack,
        snackbarHostState = snackbarHostState
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EditNoteContent(
        uiState: EditNoteUiState,
        snackbarHostState: SnackbarHostState,
        onIntent: (EditNoteIntent) -> Unit,
        onBackPress: () -> Unit
) {
    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }, topBar = {
        HomeTopBar(
            style = TopBarStyle.Edit,
            onBack = onBackPress,
            addReminder = { onIntent(EditNoteIntent.ToggleReminderBottomSheet) },
        )
    }, bottomBar = {
        EditNoteBottomBar(
            onLabelsClick = { onIntent(EditNoteIntent.ToggleLabelDialog) },
            onSaveClick = { onIntent(EditNoteIntent.SaveNote(null)) })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {


            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                uiState.reminder?.let { reminder ->
                    ReminderChip(
                        reminder = reminder.toReminderString(),
                        onRemoveClick = { onIntent(EditNoteIntent.RemoveReminder) })
                }

                Spacer(modifier = Modifier.width(8.dp))

                uiState.labels.forEach { label ->
                    LabelChip(
                        label = label,
                        onRemoveClick = { onIntent(EditNoteIntent.RemoveLabel(label)) })
                }
            }


            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = uiState.title,
                onValueChange = { onIntent(EditNoteIntent.UpdateTitle(it)) },
                placeholder = { Text(stringResource(R.string.title)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                )
            )

            Spacer(modifier = Modifier.height(8.dp))


            TextField(
                value = uiState.content,
                onValueChange = { onIntent(EditNoteIntent.UpdateContent(it)) },
                placeholder = { Text("Note") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                )
            )

        }
    }
}

@Composable
fun AddLabelDialog(
        onDismiss: () -> Unit, onConfirm: (String) -> Unit
) {
    var label by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Label", color = MaterialTheme.colorScheme.onBackground) },
        text = {
            TextField(
                value = label,
                onValueChange = { label = it },
                placeholder = { Text("Enter label") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (label.isNotBlank()) {
                        onConfirm(label)
                    }
                }) {
                Text("Save", color = MaterialTheme.colorScheme.onBackground)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onBackground)
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
                colors = ButtonDefaults.textButtonColors()
                    .copy(containerColor = MaterialTheme.colorScheme.background), onClick = {
                    onPickDateClick()
                    onDismiss()
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pick a date", textAlign = TextAlign.Start)
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
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            dateTime.toReminderString(),
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge
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
    var expanded by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = selectedTime.hour, initialMinute = selectedTime.minute
    )
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.atStartOfDay().toInstant(ZoneOffset.UTC)
            .toEpochMilli()
    )

    // Main Dialog Content
    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(
            "Add Reminder", color = MaterialTheme.colorScheme.onBackground
        )
    }, text = {
        Column { // Date Selection
            OutlinedButton(
                onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Time Selection
            OutlinedButton(
                onClick = { showTimePicker = true }, modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Repeat Interval
            ExposedDropdownMenuBox(
                expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = selectedRepeatInterval.name,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded, onDismissRequest = { expanded = false }) {
                    RepeatInterval.entries.forEach { interval ->
                        DropdownMenuItem(text = {
                            Text(
                                interval.name, color = MaterialTheme.colorScheme.onBackground
                            )
                        }, onClick = {
                            onRepeatIntervalSelected(interval)
                            expanded = false
                        })
                    }
                }
            }
        }
    }, confirmButton = {
        TextButton(onClick = onSave) {
            Text("Save", color = MaterialTheme.colorScheme.onBackground)
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel", color = MaterialTheme.colorScheme.onBackground)
        }
    })

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val newSelectedDate = Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(kotlinx.datetime.TimeZone.UTC).date.let { kotlinxDate ->
                                LocalDate.of(
                                    kotlinxDate.year,
                                    kotlinxDate.monthNumber,
                                    kotlinxDate.dayOfMonth
                                )
                            }
                        onDateSelected(newSelectedDate)
                    }
                    showDatePicker = false
                }) {
                Text("OK")
            }
        }, dismissButton = {
            TextButton(
                onClick = { showDatePicker = false }) {
                Text("Cancel")
            }
        }) {
            DatePicker(
                state = datePickerState, showModeToggle = false
            )
        }
    }

    // Time Picker Dialog
    if (showTimePicker) {
        AlertDialog(title = { Text("Pick time") }, text = {
            TimeInput(
                state = timePickerState
            )
        }, onDismissRequest = { showTimePicker = false }, confirmButton = {
            TextButton(
                onClick = {
                    onTimeSelected(
                        LocalTime.of(
                            timePickerState.hour, timePickerState.minute
                        )
                    )
                    showTimePicker = false
                }) {
                Text("OK")
            }
        }, dismissButton = {
            TextButton(
                onClick = { showTimePicker = false }) {
                Text("Cancel")
            }
        })
    }
}

@Composable
fun EditNoteBottomBar(
        onLabelsClick: () -> Unit, onSaveClick: () -> Unit, modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onLabelsClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tag),
                    contentDescription = "Labels",
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Labels",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            FloatingActionButton(
                onClick = onSaveClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tick),
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}
