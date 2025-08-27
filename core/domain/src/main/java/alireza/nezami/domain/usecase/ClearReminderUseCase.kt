package alireza.nezami.domain.usecase

import alireza.nezami.data.repository.NoteRepository
import javax.inject.Inject

class ClearReminderUseCase @Inject constructor(
        private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long) = repository.clearReminder(noteId)
}