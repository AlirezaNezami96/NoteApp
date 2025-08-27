package alireza.nezami.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

// Domain model for UI and business logic
@Parcelize
data class Note(
        val id: Long = 0,
        val title: String,
        val content: String,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val reminder: Reminder? = null,
        val labels: List<String> = emptyList()
) : Parcelable

@Parcelize
data class Reminder(
        val time: LocalDateTime,
        val isEnabled: Boolean,
        val repeatInterval: RepeatInterval = RepeatInterval.NONE
) : Parcelable