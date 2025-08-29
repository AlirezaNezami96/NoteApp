package alireza.nezami.home.presentation.contract

import alireza.nezami.model.domain.Note


sealed class HomeEvent {
    data class NavigateToEditNote(val note: Note?) : HomeEvent()
}
