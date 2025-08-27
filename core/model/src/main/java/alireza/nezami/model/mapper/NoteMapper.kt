package alireza.nezami.model.mapper

import alireza.nezami.model.domain.Note
import alireza.nezami.model.domain.Reminder
import alireza.nezami.model.domain.RepeatInterval
import alireza.nezami.model.entity.NoteEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

// Mapper to convert between Entity and Domain models
object NoteMapper {
    fun Note.toEntity() = NoteEntity(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt.toInstant(ZoneOffset.UTC).toEpochMilli(),
        updatedAt = updatedAt.toInstant(ZoneOffset.UTC).toEpochMilli(),
        reminderTime = reminder?.time?.toInstant(ZoneOffset.UTC)?.toEpochMilli(),
        isReminderSet = reminder?.isEnabled ?: false,
        repeatInterval = reminder?.repeatInterval?.name,
        labels = if (labels.isEmpty()) null else labels.joinToString(",")
    )

    fun NoteEntity.toDomain() = Note(
        id = id,
        title = title,
        content = content,
        createdAt = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(createdAt),
            ZoneId.systemDefault()
        ),
        updatedAt = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(updatedAt),
            ZoneId.systemDefault()
        ),
        reminder = reminderTime?.let { time ->
            Reminder(
                time = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(time),
                    ZoneId.systemDefault()
                ),
                isEnabled = isReminderSet,
                repeatInterval = repeatInterval?.let {
                    RepeatInterval.valueOf(it)
                } ?: RepeatInterval.NONE
            )
        },
        labels = labels?.split(",")?.map { it.trim() } ?: emptyList()
    )
}