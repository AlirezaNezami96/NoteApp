package alireza.nezami.common.extensions

import alireza.nezami.model.domain.Note
import alireza.nezami.model.domain.Reminder
import alireza.nezami.model.domain.RepeatInterval
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Random

/**
 * Converts a LocalDateTime to a human-readable string based on device time
 * Examples: "Today, 18:00", "Tomorrow, 20:00", "Wednesday, 17:00", "September 21", "2026/02/23"
 */
fun LocalDateTime.toReminderString(): String {
    val now = LocalDateTime.now()

    // Check if the reminder is in the past
    if (this.isBefore(now)) {
        return "Past reminder"
    }

    val daysDifference = ChronoUnit.DAYS.between(now.toLocalDate(), this.toLocalDate())
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val timeString = this.format(timeFormatter)

    return when (daysDifference.toInt()) {
        0 -> "Today, $timeString"
        1 -> "Tomorrow, $timeString"

        in 2..6 -> {
            val dayFormatter = DateTimeFormatter.ofPattern("EEEE")
            val dayName = this.format(dayFormatter)
            "$dayName, $timeString"
        }

        in 7..365 -> { // Same year - show month and day
            if (this.year == now.year) {
                val monthDayFormatter = DateTimeFormatter.ofPattern("MMMM d")
                this.format(monthDayFormatter)
            } else {
                val fullDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                this.format(fullDateFormatter)
            }
        }

        else -> { // More than a year away - show full date
            val fullDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            this.format(fullDateFormatter)
        }
    }
}

/**
 * Converts a LocalDateTime to a detailed human-readable string with more granular time display
 * Examples: "In 15m", "In 2h", "Today, 18:00", "Tomorrow, 20:00", "Wednesday, 17:00"
 */
fun LocalDateTime.toDetailedReminderString(): String {
    val now = LocalDateTime.now()

    // Check if the reminder is in the past
    if (this.isBefore(now)) {
        return "Past reminder"
    }

    val daysDifference = ChronoUnit.DAYS.between(now.toLocalDate(), this.toLocalDate())
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val timeString = this.format(timeFormatter)

    return when (daysDifference.toInt()) {
        0 -> { // Same day - show granular time for near future
            val minutesDifference = ChronoUnit.MINUTES.between(now, this)
            val hoursDifference = ChronoUnit.HOURS.between(now, this)

            when {
                minutesDifference < 1 -> "Now"
                minutesDifference < 60 -> "In ${minutesDifference}m"
                hoursDifference < 12 -> "In ${hoursDifference}h"
                else -> "Today, $timeString"
            }
        }

        1 -> { // Tomorrow - show time for early reminders, or just "Tomorrow" for far ones
            val totalHours = ChronoUnit.HOURS.between(now, this)
            when {
                totalHours < 36 -> "Tomorrow, $timeString"
                else -> "Tomorrow, $timeString"
            }
        }

        in 2..6 -> {
            val dayFormatter = DateTimeFormatter.ofPattern("EEEE")
            val dayName = this.format(dayFormatter)
            "$dayName, $timeString"
        }

        in 7..30 -> { // This month - show date and time for closer dates
            val monthDayFormatter = DateTimeFormatter.ofPattern("MMM d")
            val monthDayString = this.format(monthDayFormatter)
            "$monthDayString, $timeString"
        }

        in 31..365 -> { // Same year - show month and day
            if (this.year == now.year) {
                val monthDayFormatter = DateTimeFormatter.ofPattern("MMMM d")
                this.format(monthDayFormatter)
            } else {
                val fullDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                this.format(fullDateFormatter)
            }
        }

        else -> { // More than a year away - show full date
            val fullDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            this.format(fullDateFormatter)
        }
    }
}

