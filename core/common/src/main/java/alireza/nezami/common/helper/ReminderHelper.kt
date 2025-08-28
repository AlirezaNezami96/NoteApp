package alireza.nezami.common.helper

import alireza.nezami.common.extensions.plusDays
import alireza.nezami.common.extensions.plusMonths
import alireza.nezami.common.extensions.plusWeeks
import alireza.nezami.common.extensions.plusYears
import alireza.nezami.model.domain.RepeatInterval
import kotlinx.datetime.*
import javax.inject.Inject

class ReminderHelper @Inject constructor() {
    private val zone = TimeZone.currentSystemDefault()

    fun calculateNextReminderTime(
            currentTime: LocalDateTime,
            repeatInterval: RepeatInterval
    ): LocalDateTime = when (repeatInterval) {
        RepeatInterval.NONE -> currentTime
        RepeatInterval.DAILY -> currentTime.plusDays(1, zone)
        RepeatInterval.WEEKDAYS -> {
            var nextTime = currentTime.plusDays(1, zone)
            while (nextTime.dayOfWeek.isoDayNumber > 5) { // 6=Saturday, 7=Sunday
                nextTime = nextTime.plusDays(1, zone)
            }
            nextTime
        }
        RepeatInterval.WEEKENDS -> {
            var nextTime = currentTime.plusDays(1, zone)
            while (nextTime.dayOfWeek.isoDayNumber <= 5) {
                nextTime = nextTime.plusDays(1, zone)
            }
            nextTime
        }
        RepeatInterval.WEEKLY -> currentTime.plusWeeks(1, zone)
        RepeatInterval.BIWEEKLY -> currentTime.plusWeeks(2, zone)
        RepeatInterval.MONTHLY -> currentTime.plusMonths(1, zone)
        RepeatInterval.QUARTERLY -> currentTime.plusMonths(3, zone)
        RepeatInterval.BIANNUALLY -> currentTime.plusMonths(6, zone)
        RepeatInterval.YEARLY -> currentTime.plusYears(1, zone)
    }
}
