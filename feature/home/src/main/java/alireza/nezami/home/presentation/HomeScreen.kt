package alireza.nezami.home.presentation

import alireza.nezami.designsystem.R
import alireza.nezami.designsystem.components.HomeTopBar
import alireza.nezami.designsystem.components.LayoutType
import alireza.nezami.home.presentation.contract.HomeIntent
import alireza.nezami.home.presentation.contract.HomesUiState
import alireza.nezami.model.domain.Note
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
        viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
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
        },
        bottomBar = {
            HomeBottomBar(
                onLabelsClick = { onIntent(HomeIntent.OnLabelsClick) },
                onAddClick = { onIntent(HomeIntent.OnAddClick) }
            )
        }
    ) { paddingValues ->
        NoteList(
            modifier = Modifier.padding(paddingValues),
            notes = uiState.notes,
            layoutType = uiState.layoutType,
            onNoteClick = { note -> onIntent(HomeIntent.OnNoteClick(note)) }
        )
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
            contentPadding = PaddingValues(8.dp),
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

@Composable
fun NoteCard(note: Note, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(note.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(
                note.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            note.reminder?.let {
                Spacer(Modifier.height(8.dp))
                AssistChip(onClick = {}, label = {
                    Text(
                        it.time.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                })
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
