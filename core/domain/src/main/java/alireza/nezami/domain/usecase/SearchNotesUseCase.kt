package alireza.nezami.domain.usecase

import alireza.nezami.domain.repository.NoteRepository
import alireza.nezami.model.domain.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchNotesUseCase @Inject constructor(
        private val noteRepository: NoteRepository
) {
    operator fun invoke(query: String): Flow<List<Note>> = noteRepository.searchNotes(query)
}