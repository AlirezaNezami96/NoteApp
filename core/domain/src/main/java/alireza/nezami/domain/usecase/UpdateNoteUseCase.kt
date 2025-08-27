package alireza.nezami.domain.usecase

import alireza.nezami.data.repository.NoteRepository
import alireza.nezami.model.domain.Note
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
        private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) = repository.updateNote(note)
}