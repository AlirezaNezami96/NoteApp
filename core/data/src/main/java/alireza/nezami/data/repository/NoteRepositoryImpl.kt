package alireza.nezami.data.repository

import alireza.nezami.common.helper.ReminderHelper
import alireza.nezami.database.dao.NoteDao
import alireza.nezami.domain.repository.NoteRepository
import alireza.nezami.model.domain.Note
import alireza.nezami.model.domain.RepeatInterval
import alireza.nezami.model.mapper.NoteMapper.toDomain
import alireza.nezami.model.mapper.NoteMapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
        private val noteDao: NoteDao,
        private val reminderHelper: ReminderHelper
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> =
        noteDao.getAllNotes().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getUpcomingReminders(currentTime: Long): Flow<List<Note>> =
        noteDao.getUpcomingReminders(currentTime).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getNoteById(id: Long): Note? =
        noteDao.getNoteById(id)?.toDomain()

    override suspend fun insertNote(note: Note): Long =
        noteDao.insertNote(note.toEntity())

    override suspend fun updateNote(note: Note) =
        noteDao.updateNote(note.toEntity())

    override suspend fun deleteNote(note: Note) =
        noteDao.deleteNote(note.toEntity())

    override fun searchNotes(query: String): Flow<List<Note>> =
        noteDao.searchNotes(query).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun handleReminderNotification(noteId: Long) {
        val note = noteDao.getNoteById(noteId)?.toDomain()

        note?.reminder?.let { reminder ->
            if (reminder.repeatInterval != RepeatInterval.NONE) {
                val nextReminderTime = reminderHelper.calculateNextReminderTime(
                    reminder.time,
                    reminder.repeatInterval
                )
                updateReminderTime(noteId, nextReminderTime)
            } else {
                clearReminder(noteId)
            }
        }
    }

    override suspend fun updateReminderTime(noteId: Long, reminderTime: LocalDateTime) {
        noteDao.updateReminderTime(
            noteId,
            reminderTime.toInstant(ZoneOffset.UTC).toEpochMilli()
        )
    }

    override suspend fun clearReminder(noteId: Long) {
        noteDao.clearReminder(noteId)
    }
}