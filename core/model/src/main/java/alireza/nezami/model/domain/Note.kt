package alireza.nezami.model.domain

import java.time.LocalDateTime

// Domain model for UI and business logic
data class Note(
        val id: Long = 0,
        val title: String,
        val content: String,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val reminder: Reminder? = null,
        val labels: List<String> = emptyList()
)

data class Reminder(
        val time: LocalDateTime,
        val isEnabled: Boolean,
        val repeatInterval: RepeatInterval = RepeatInterval.NONE
)