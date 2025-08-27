package alireza.nezami.domain.usecase

import alireza.nezami.data.repository.NoteRepository
import alireza.nezami.model.domain.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> = repository.getAllNotes()
}