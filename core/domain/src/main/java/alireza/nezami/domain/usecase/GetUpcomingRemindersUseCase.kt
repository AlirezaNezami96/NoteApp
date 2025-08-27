package alireza.nezami.domain.usecase

import alireza.nezami.domain.repository.NoteRepository
import alireza.nezami.model.domain.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUpcomingRemindersUseCase @Inject constructor(
        private val repository: NoteRepository
) {
    operator fun invoke(currentTime: Long): Flow<List<Note>> =
        repository.getUpcomingReminders(currentTime)
}