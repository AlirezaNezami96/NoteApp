package alireza.nezami.domain.usecase

import alireza.nezami.data.repository.NoteRepository
import java.time.LocalDateTime
import javax.inject.Inject

class UpdateReminderTimeUseCase @Inject constructor(
        private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long, reminderTime: LocalDateTime) =
        repository.updateReminderTime(noteId, reminderTime)
}