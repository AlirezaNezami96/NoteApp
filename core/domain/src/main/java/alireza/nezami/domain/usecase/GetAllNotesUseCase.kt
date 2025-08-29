package alireza.nezami.domain.usecase

import alireza.nezami.common.mock.NoteMockProvider
import alireza.nezami.domain.repository.NoteRepository
import alireza.nezami.model.domain.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
        private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> = flow {
        repository.getAllNotes().map { repoNotes ->
                val mockNotes = NoteMockProvider.provideMockNotes()
                val maxRepoId = repoNotes.maxByOrNull { it.id }?.id ?: 0
                val adjustedMockNotes = mockNotes.map { mockNote ->
                    mockNote.copy(id = mockNote.id + maxRepoId)
                }
                repoNotes + adjustedMockNotes
            }.collect { combinedNotes ->
                emit(combinedNotes)
            }
    }
}