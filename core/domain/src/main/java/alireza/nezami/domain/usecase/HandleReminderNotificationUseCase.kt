package alireza.nezami.domain.usecase

import alireza.nezami.domain.repository.NoteRepository
import javax.inject.Inject

class HandleReminderNotificationUseCase @Inject constructor(
        private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long) = repository.handleReminderNotification(noteId)
}