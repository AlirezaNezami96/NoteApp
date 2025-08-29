package alireza.nezami.domain.usecase

import alireza.nezami.domain.repository.NoteRepository
import alireza.nezami.model.domain.Note
import javax.inject.Inject

class InsertNoteUseCase @Inject constructor(
        private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note): Long = repository.insertNote(note)
}