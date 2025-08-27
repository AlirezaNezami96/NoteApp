package alireza.nezami.home.presentation
import alireza.nezami.common.utils.base.BaseViewModel
import alireza.nezami.designsystem.components.LayoutType
import alireza.nezami.designsystem.components.TopBarStyle
import alireza.nezami.domain.usecase.GetAllNotesUseCase
import alireza.nezami.domain.usecase.SearchNotesUseCase
import alireza.nezami.home.presentation.contract.HomeEvent
import alireza.nezami.home.presentation.contract.HomeIntent
import alireza.nezami.home.presentation.contract.HomesUiState
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNotesUseCase: GetAllNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase
) : BaseViewModel<HomesUiState, HomesUiState.HomePartialState, HomeEvent, HomeIntent>(
    savedStateHandle, HomesUiState()
) {

    init {
        acceptIntent(HomeIntent.LoadNotes)
    }

    override fun mapIntents(intent: HomeIntent): Flow<HomesUiState.HomePartialState> = when (intent) {
        is HomeIntent.LoadNotes -> getNotes()
        is HomeIntent.OnSearchClick -> flow {
            emit(HomesUiState.HomePartialState.TopBarStyleChanged(TopBarStyle.Search))
        }
        is HomeIntent.OnBackClick -> flow {
            emit(HomesUiState.HomePartialState.TopBarStyleChanged(TopBarStyle.Home))
            acceptIntent(HomeIntent.LoadNotes)
            emit(HomesUiState.HomePartialState.SearchCleared)
        }
        is HomeIntent.OnSearchQueryChange -> searchNotesWithQuery(intent.query)
        is HomeIntent.OnClearSearch -> flow {
            emit(HomesUiState.HomePartialState.SearchCleared)
            emit(HomesUiState.HomePartialState.TopBarStyleChanged(TopBarStyle.Home))
        }
        is HomeIntent.OnSwitchLayout -> flow {
            emit(HomesUiState.HomePartialState.LayoutChanged(uiState.value.layoutType == LayoutType.Grid))
        }
        is HomeIntent.OnNoteClick -> flow {
            publishEvent(HomeEvent.NavigateToEditNote(intent.note))
            emptyFlow<HomesUiState.HomePartialState>()
        }
        is HomeIntent.OnLabelsClick -> flow {
            emit(HomesUiState.HomePartialState.LabelsClicked)
        }
        is HomeIntent.OnAddClick -> flow {
            publishEvent(HomeEvent.NavigateToEditNote(null))
            emptyFlow<HomesUiState.HomePartialState>()
        }
    }

    override fun reduceUiState(
            previousState: HomesUiState,
            partialState: HomesUiState.HomePartialState
    ): HomesUiState = when (partialState) {
        is HomesUiState.HomePartialState.NotesLoaded -> previousState.copy(
            notes = partialState.notes,
            isLoading = false
        )
        is HomesUiState.HomePartialState.NotesLoading -> previousState.copy(
            isLoading = partialState.show
        )
        is HomesUiState.HomePartialState.SearchQueryChanged -> previousState.copy(
            searchQuery = partialState.query
        )
        is HomesUiState.HomePartialState.SearchResults -> previousState.copy(
            notes = partialState.results
        )
        is HomesUiState.HomePartialState.SearchCleared -> previousState.copy(
            searchQuery = "",
            notes = emptyList()
        )
        is HomesUiState.HomePartialState.TopBarStyleChanged -> previousState.copy(
            topBarStyle = partialState.style
        )
        is HomesUiState.HomePartialState.LayoutChanged -> previousState.copy(
            layoutType = if (partialState.isGrid) LayoutType.Grid else LayoutType.List
        )
        else -> previousState
    }

    private fun getNotes(): Flow<HomesUiState.HomePartialState> = flow {
        getNotesUseCase().map { result -> emit(HomesUiState.HomePartialState.NotesLoaded(result))
        }.catch {
            emit(HomesUiState.HomePartialState.NotesError(it.message.orEmpty()))
        }.collect()
    }

    private fun searchNotesWithQuery(query: String): Flow<HomesUiState.HomePartialState> = flow {
        emit(HomesUiState.HomePartialState.SearchQueryChanged(query))
        searchNotesUseCase(query).map { result -> emit(HomesUiState.HomePartialState.NotesLoaded(result))
        }.catch {
            emit(HomesUiState.HomePartialState.NotesError(it.message.orEmpty()))
        }.collect()
    }
}