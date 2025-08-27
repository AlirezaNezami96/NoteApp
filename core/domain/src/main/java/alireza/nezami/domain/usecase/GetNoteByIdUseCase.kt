package alireza.nezami.domain.usecase

import alireza.nezami.data.repository.NoteRepository
import alireza.nezami.model.domain.Note
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
        private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Long): Note? = repository.getNoteById(id)
}