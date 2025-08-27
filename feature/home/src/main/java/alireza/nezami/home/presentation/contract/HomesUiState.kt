package alireza.nezami.home.presentation.contract

import alireza.nezami.designsystem.components.LayoutType
import alireza.nezami.designsystem.components.TopBarStyle
import alireza.nezami.model.domain.Note
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class HomesUiState(
        val isLoading: Boolean = false,
        val notes: List<Note> = emptyList(),
        val topBarStyle: TopBarStyle = TopBarStyle.Home,
        val layoutType: LayoutType = LayoutType.Grid,
        val searchQuery: String = "",
) : Parcelable {

    sealed class HomePartialState {
        data class NotesLoaded(val notes: List<Note>) : HomePartialState()
        data class NotesLoading(val show: Boolean) : HomePartialState()
        data class NotesError(val message: String) : HomePartialState()
        data class SearchQueryChanged(val query: String) : HomePartialState()
        data class SearchResults(val results: List<Note>) : HomePartialState()
        data object SearchCleared : HomePartialState()
        data class TopBarStyleChanged(val style: TopBarStyle) : HomePartialState()
        data class LayoutChanged(val isGrid: Boolean) : HomePartialState()
        data object LabelsClicked : HomePartialState()
    }
}
