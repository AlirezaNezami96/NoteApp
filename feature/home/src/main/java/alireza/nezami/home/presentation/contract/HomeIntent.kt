package alireza.nezami.home.presentation.contract


import alireza.nezami.model.domain.Note

sealed class HomeIntent {
    data object OnSearchClick : HomeIntent()
    data object OnClearSearch : HomeIntent()
    data object OnBackClick : HomeIntent()
    data class OnSearchQueryChange(val query: String) : HomeIntent()
    data object OnSwitchLayout : HomeIntent()
    data class OnNoteClick(val note: Note) : HomeIntent()
    data object OnLabelsClick : HomeIntent()
    data object OnAddClick : HomeIntent()
    data object LoadNotes : HomeIntent()
}
