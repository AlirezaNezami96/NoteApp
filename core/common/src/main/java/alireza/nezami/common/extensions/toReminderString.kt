package alireza.nezami.common.extensions

/**
 * Converts a LocalDateTime to a human-readable string based on device time
 * Examples: "Today, 18:00", "Tomorrow, 20:00", "Wednesday, 17:00", "September 21", "2026/02/23"
 */
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until

fun LocalDateTime.toReminderString(): String {
    val zone = TimeZone.currentSystemDefault()
    val now = Clock.System.now().toLocalDateTime(zone)

    // Past check
    if (this < now) return "Past reminder"

    val daysDifference = now.date.daysUntil(this.date)
    val timeString = "%02d:%02d".format(this.hour, this.minute)

    return when (daysDifference) {
        0 -> "Today, $timeString"
        1 -> "Tomorrow, $timeString"
        in 2..6 -> {
            val dayName = this.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
            "$dayName, $timeString"
        }

        in 7..365 -> {
            if (this.year == now.year) {
                "${
                    this.month.name.lowercase().replaceFirstChar { it.uppercase() }
                } ${this.dayOfMonth}"
            } else {
                "${this.year}/${this.monthNumber}/${this.dayOfMonth}"
            }
        }

        else -> "${this.year}/${this.monthNumber}/${this.dayOfMonth}"
    }
}

/**
 * Converts a LocalDateTime to a detailed human-readable string with more granular time display
 * Examples: "In 15m", "In 2h", "Today, 18:00", "Tomorrow, 20:00", "Wednesday, 17:00"
 */
fun LocalDateTime.toDetailedReminderString(): String {
    val zone = TimeZone.currentSystemDefault()
    val now = Clock.System.now().toLocalDateTime(zone)

    if (this < now) return "Past reminder"

    val daysDifference = now.date.daysUntil(this.date)
    val minutesDifference = now.toInstant(zone).until(this.toInstant(zone), DateTimeUnit.MINUTE)
    val hoursDifference = now.toInstant(zone).until(this.toInstant(zone), DateTimeUnit.HOUR)
    val timeString = "%02d:%02d".format(this.hour, this.minute)

    return when (daysDifference) {
        0 -> when {
            minutesDifference < 1 -> "Now"
            minutesDifference < 60 -> "In ${minutesDifference}m"
            hoursDifference < 12 -> "In ${hoursDifference}h"
            else -> "Today, $timeString"
        }

        1 -> "Tomorrow, $timeString"
        in 2..6 -> {
            val dayName = this.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
            "$dayName, $timeString"
        }

        in 7..30 -> {
            val monthName = this.month.name.lowercase().replaceFirstChar { it.uppercase() }
            "$monthName ${this.dayOfMonth}, $timeString"
        }

        in 31..365 -> {
            if (this.year == now.year) {
                val monthName = this.month.name.lowercase().replaceFirstChar { it.uppercase() }
                "$monthName ${this.dayOfMonth}"
            } else {
                "${this.year}/${this.monthNumber}/${this.dayOfMonth}"
            }
        }

        else -> "${this.year}/${this.monthNumber}/${this.dayOfMonth}"
    }
}

fun LocalDateTime.plusDays(
        days: Int,
        zone: TimeZone = TimeZone.currentSystemDefault()
): LocalDateTime = this.toInstant(zone).plus(days, DateTimeUnit.DAY, zone).toLocalDateTime(zone)

fun LocalDateTime.plusWeeks(
        weeks: Int,
        zone: TimeZone = TimeZone.currentSystemDefault()
): LocalDateTime = this.toInstant(zone).plus(weeks, DateTimeUnit.WEEK, zone).toLocalDateTime(zone)

fun LocalDateTime.plusMonths(
        months: Int,
        zone: TimeZone = TimeZone.currentSystemDefault()
): LocalDateTime = this.toInstant(zone).plus(months, DateTimeUnit.MONTH, zone).toLocalDateTime(zone)

fun LocalDateTime.plusYears(
        years: Int,
        zone: TimeZone = TimeZone.currentSystemDefault()
): LocalDateTime = this.toInstant(zone).plus(years, DateTimeUnit.YEAR, zone).toLocalDateTime(zone)


fun LocalDateTime.plusDays(days: Long, zone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime =
    this.toInstant(zone).plus(days, DateTimeUnit.DAY, zone).toLocalDateTime(zone)

fun LocalDateTime.minusDays(days: Long, zone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime =
    this.toInstant(zone).minus(days, DateTimeUnit.DAY, zone).toLocalDateTime(zone)

fun LocalDateTime.plusMonths(months: Long, zone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime =
    this.toInstant(zone).plus(months, DateTimeUnit.MONTH, zone).toLocalDateTime(zone)

fun LocalDateTime.plusYears(years: Long, zone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime =
    this.toInstant(zone).plus(years, DateTimeUnit.YEAR, zone).toLocalDateTime(zone)

fun LocalDateTime.plusHours(hours: Long, zone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime =
    this.toInstant(zone).plus(hours, DateTimeUnit.HOUR, zone).toLocalDateTime(zone)

fun LocalDateTime.plusMinutes(minutes: Long, zone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime =
    this.toInstant(zone).plus(minutes, DateTimeUnit.MINUTE, zone).toLocalDateTime(zone)

fun LocalDateTime.withHour(hour: Int): LocalDateTime =
    LocalDateTime(year, monthNumber, dayOfMonth, hour, minute, second, nanosecond)

fun LocalDateTime.withMinute(minute: Int): LocalDateTime =
    LocalDateTime(year, monthNumber, dayOfMonth, hour, minute, second, nanosecond)
