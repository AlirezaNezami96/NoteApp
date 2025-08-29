package alireza.nezami.home.presentation

import alireza.nezami.common.extensions.toDetailedReminderString
import alireza.nezami.designsystem.R
import alireza.nezami.designsystem.components.HomeTopBar
import alireza.nezami.designsystem.components.LabelChip
import alireza.nezami.designsystem.components.LayoutType
import alireza.nezami.designsystem.components.ReminderChip
import alireza.nezami.home.presentation.contract.HomeEvent
import alireza.nezami.home.presentation.contract.HomeIntent
import alireza.nezami.home.presentation.contract.HomesUiState
import alireza.nezami.model.domain.Note
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
        viewModel: HomeViewModel = hiltViewModel(), onNoteClick: (Note?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val eventFlow = viewModel.event

    LaunchedEffect(Unit) {
        eventFlow.collectLatest { event ->
            when (event) {
                is HomeEvent.NavigateToEditNote -> onNoteClick(event.note)
            }
        }
    }

    HomeContent(
        uiState = uiState,
        onIntent = viewModel::acceptIntent,
    )
}

@Composable
fun HomeContent(
        uiState: HomesUiState,
        onIntent: (HomeIntent) -> Unit,
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background, topBar = {
        HomeTopBar(
            style = uiState.topBarStyle,
            layoutType = uiState.layoutType,
            onBack = { onIntent(HomeIntent.OnBackClick) },
            onSwitchLayout = { onIntent(HomeIntent.OnSwitchLayout) },
            onSearch = { query -> onIntent(HomeIntent.OnSearchQueryChange(query)) },
            addReminder = {},
            onSearchClick = { onIntent(HomeIntent.OnSearchClick) },
            clearSearch = { onIntent(HomeIntent.OnClearSearch) },
            searchQuery = uiState.searchQuery
        )
    }, bottomBar = {
        HomeBottomBar(
            onLabelsClick = { onIntent(HomeIntent.OnLabelsClick) },
            onAddClick = { onIntent(HomeIntent.OnAddClick) })
    }) { paddingValues ->
        NoteList(
            modifier = Modifier.padding(paddingValues),
            notes = uiState.notes,
            layoutType = uiState.layoutType,
            onNoteClick = { note -> onIntent(HomeIntent.OnNoteClick(note)) })
    }
}

@Composable
fun NoteList(
        modifier: Modifier = Modifier,
        notes: List<Note>,
        layoutType: LayoutType,
        onNoteClick: (Note) -> Unit
) {
    if (layoutType == LayoutType.Grid) {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes) { note ->
                NoteCard(note = note, onClick = { onNoteClick(note) })
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes) { note ->
                NoteCard(note = note, onClick = { onNoteClick(note) })
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteCard(note: Note, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                note.title.ifBlank { "Untitled" },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(16.dp))
            Text(
                note.content.ifBlank { "No content" },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.surfaceVariant,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

            if (note.reminder != null || note.labels.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    note.reminder?.let { reminder ->
                        ReminderChip(
                            showRemoveButton = false,
                            reminder = reminder.time.toDetailedReminderString(),
                            onRemoveClick = { }
                        )
                    }

                    note.labels.forEach { label ->
                        LabelChip(
                            showRemoveButton = false,
                            label = label,
                            onRemoveClick = { }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeBottomBar(
        onLabelsClick: () -> Unit, onAddClick: () -> Unit, modifier: Modifier = Modifier
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
