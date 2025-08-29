package alireza.nezami.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity for Room database
@Entity(tableName = "notes")
data class NoteEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val title: String,
        val content: String,
        val createdAt: Long,
        val updatedAt: Long,
        val reminderTime: Long? = null,
        val isReminderSet: Boolean = false,
        val repeatInterval: String? = null,
        val labels: String? = null
)