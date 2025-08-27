package alireza.nezami.data.repository

import alireza.nezami.model.domain.Note
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun getUpcomingReminders(currentTime: Long): Flow<List<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(note: Note): Long
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun handleReminderNotification(noteId: Long)
    suspend fun updateReminderTime(noteId: Long, reminderTime: LocalDateTime)
    suspend fun clearReminder(noteId: Long)
}