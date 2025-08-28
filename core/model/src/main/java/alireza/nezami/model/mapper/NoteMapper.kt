package alireza.nezami.model.mapper

import alireza.nezami.model.domain.Note
import alireza.nezami.model.domain.Reminder
import alireza.nezami.model.domain.RepeatInterval
import alireza.nezami.model.entity.NoteEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

// Mapper to convert between Entity and Domain models
object NoteMapper {
    fun Note.toEntity() = NoteEntity(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
        updatedAt = updatedAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
        reminderTime = reminder?.time?.toInstant(TimeZone.currentSystemDefault())
            ?.toEpochMilliseconds(),
        isReminderSet = reminder?.isEnabled ?: false,
        repeatInterval = reminder?.repeatInterval?.name,
        labels = if (labels.isEmpty()) null else labels.joinToString(",")
    )

    fun NoteEntity.toDomain() = Note(
        id = id,
        title = title,
        content = content,
        createdAt = Instant.fromEpochMilliseconds(createdAt)
            .toLocalDateTime(TimeZone.currentSystemDefault()),
        updatedAt = Instant.fromEpochMilliseconds(updatedAt)
            .toLocalDateTime(TimeZone.currentSystemDefault()),
        reminder = reminderTime?.let { time ->
            Reminder(
                time = Instant.fromEpochMilliseconds(time)
                .toLocalDateTime(TimeZone.currentSystemDefault()),
                isEnabled = isReminderSet,
                repeatInterval = repeatInterval?.let {
                    RepeatInterval.valueOf(it)
                } ?: RepeatInterval.NONE)
        },
        labels = labels?.split(",")?.map { it.trim() } ?: emptyList())
}