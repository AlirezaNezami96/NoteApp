package alireza.nezami.common.helper

import alireza.nezami.model.domain.RepeatInterval
import java.time.LocalDateTime

class ReminderHelper {
    fun calculateNextReminderTime(
            currentTime: LocalDateTime, repeatInterval: RepeatInterval
    ): LocalDateTime = when (repeatInterval) {
        RepeatInterval.NONE -> currentTime
        RepeatInterval.DAILY -> currentTime.plusDays(1)
        RepeatInterval.WEEKDAYS -> {
            var nextTime = currentTime.plusDays(1)
            while (nextTime.dayOfWeek.value > 5) {
                nextTime = nextTime.plusDays(1)
            }
            nextTime
        }

        RepeatInterval.WEEKENDS -> {
            var nextTime = currentTime.plusDays(1)
            while (nextTime.dayOfWeek.value <= 5) {
                nextTime = nextTime.plusDays(1)
            }
            nextTime
        }

        RepeatInterval.WEEKLY -> currentTime.plusWeeks(1)
        RepeatInterval.BIWEEKLY -> currentTime.plusWeeks(2)
        RepeatInterval.MONTHLY -> currentTime.plusMonths(1)
        RepeatInterval.QUARTERLY -> currentTime.plusMonths(3)
        RepeatInterval.BIANNUALLY -> currentTime.plusMonths(6)
        RepeatInterval.YEARLY -> currentTime.plusYears(1)
    }
}