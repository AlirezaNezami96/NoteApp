package alireza.nezami.model.domain

import android.os.Parcelable
import kotlinx.datetime.LocalDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Note(
        val id: Long = 0,
        val title: String,
        val content: String,
        val createdAt: @RawValue LocalDateTime,
        val updatedAt: @RawValue LocalDateTime,
        val reminder: Reminder? = null,
        val labels: List<String> = emptyList()
) : Parcelable

@Serializable
@Parcelize
data class Reminder(
        @Contextual val time: @RawValue LocalDateTime,
        val isEnabled: Boolean,
        val repeatInterval: RepeatInterval = RepeatInterval.NONE
) : Parcelable