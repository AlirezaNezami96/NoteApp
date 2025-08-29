package alireza.nezami.database.dao

import alireza.nezami.model.entity.NoteEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Long): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query(
        """
        SELECT * FROM notes 
        WHERE title LIKE '%' || :query || '%' 
        OR content LIKE '%' || :query || '%' 
        ORDER BY updatedAt DESC
    """
    )
    fun searchNotes(query: String): Flow<List<NoteEntity>>

    // Reminder-specific queries
    @Query("SELECT * FROM notes WHERE isReminderSet = 1 AND reminderTime IS NOT NULL AND reminderTime > :currentTime ORDER BY reminderTime ASC")
    fun getUpcomingReminders(currentTime: Long): Flow<List<NoteEntity>>

    @Query("UPDATE notes SET reminderTime = :nextReminderTime WHERE id = :noteId")
    suspend fun updateReminderTime(noteId: Long, nextReminderTime: Long)

    @Query("UPDATE notes SET isReminderSet = 0, reminderTime = NULL, repeatInterval = NULL WHERE id = :noteId")
    suspend fun clearReminder(noteId: Long)
}