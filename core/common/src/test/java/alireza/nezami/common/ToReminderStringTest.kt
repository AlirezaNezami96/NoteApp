package alireza.nezami.common

import alireza.nezami.common.extensions.minusDays
import alireza.nezami.common.extensions.plusDays
import alireza.nezami.common.extensions.plusHours
import alireza.nezami.common.extensions.plusMinutes
import alireza.nezami.common.extensions.plusMonths
import alireza.nezami.common.extensions.plusWeeks
import alireza.nezami.common.extensions.plusYears
import alireza.nezami.common.extensions.toDetailedReminderString
import alireza.nezami.common.extensions.toReminderString
import alireza.nezami.common.extensions.withHour
import alireza.nezami.common.extensions.withMinute
import alireza.nezami.model.domain.Reminder
import alireza.nezami.model.domain.RepeatInterval
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ToReminderStringTest {

    private val fixedTimeZone = TimeZone.UTC
    private val baseInstant = Clock.System.now()
    private val baseDateTime = baseInstant.toLocalDateTime(fixedTimeZone)

    @Test
    fun `toReminderString should return Past reminder for past datetime`() {
        val pastDateTime = baseDateTime.minusDays(1)
        assertThat(pastDateTime.toReminderString()).isEqualTo("Past reminder")
    }

    @Test
    fun `toReminderString should return Today format for same day`() {
        val todayDateTime = baseDateTime.plusHours(2) // 12:30
        assertThat(todayDateTime.toReminderString()).isEqualTo("Today, 12:30")
    }

    @Test
    fun `toReminderString should return Tomorrow format for next day`() {
        val tomorrowDateTime = baseDateTime.plusDays(1) // Tuesday
        assertThat(tomorrowDateTime.toReminderString()).isEqualTo("Tomorrow, 10:30")
    }

    @Test
    fun `toReminderString should return day name for 2-6 days ahead`() {
        val wednesdayDateTime = baseDateTime.plusDays(2) // Wednesday
        assertThat(wednesdayDateTime.toReminderString()).isEqualTo("Wednesday, 10:30")

        val saturdayDateTime = baseDateTime.plusDays(5) // Saturday
        assertThat(saturdayDateTime.toReminderString()).isEqualTo("Saturday, 10:30")
    }

    @Test
    fun `toReminderString should return month day for same year within 7-365 days`() {
        val futureDateTime = baseDateTime.plusDays(30) // February 14
        assertThat(futureDateTime.toReminderString()).isEqualTo("February 14")
    }

    @Test
    fun `toReminderString should return year month day for different year within 7-365 days`() {
        val nextYearDateTime = LocalDateTime(2025, 2, 15, 10, 30)
        assertThat(nextYearDateTime.toReminderString()).isEqualTo("2025/2/15")
    }

    @Test
    fun `toReminderString should return year month day for more than 365 days`() {
        val farFutureDateTime = baseDateTime.plusDays(400)
        assertThat(farFutureDateTime.toReminderString()).isEqualTo("2025/2/19")
    }

    @Test
    fun `toReminderString should handle edge case of exactly 7 days`() {
        val nextWeekDateTime = baseDateTime.plusDays(7)
        assertThat(nextWeekDateTime.toReminderString()).isEqualTo("January 22")
    }

    @Test
    fun `toReminderString should handle midnight time`() {
        val midnightDateTime = LocalDateTime(2024, 1, 16, 0, 0)
        assertThat(midnightDateTime.toReminderString()).isEqualTo("Tomorrow, 00:00")
    }

    @Test
    fun `toReminderString should handle single digit time values`() {
        val singleDigitTime = LocalDateTime(2024, 1, 16, 9, 5)
        assertThat(singleDigitTime.toReminderString()).isEqualTo("Tomorrow, 09:05")
    }

    @Test
    fun `toDetailedReminderString should return Past reminder for past datetime`() {
        val pastDateTime = baseDateTime.minusDays(1)
        assertThat(pastDateTime.toDetailedReminderString()).isEqualTo("Past reminder")
    }

    @Test
    fun `toDetailedReminderString should return Now for current time`() {
        assertThat(baseDateTime.toDetailedReminderString()).isEqualTo("Now")
    }

    @Test
    fun `toDetailedReminderString should return minutes format for less than 1 hour`() {
        val in30Minutes = baseDateTime.plusMinutes(30)
        assertThat(in30Minutes.toDetailedReminderString()).isEqualTo("In 30m")

        val in59Minutes = baseDateTime.plusMinutes(59)
        assertThat(in59Minutes.toDetailedReminderString()).isEqualTo("In 59m")
    }

    @Test
    fun `toDetailedReminderString should return hours format for less than 12 hours`() {
        val in2Hours = baseDateTime.plusHours(2)
        assertThat(in2Hours.toDetailedReminderString()).isEqualTo("Past reminder")

        val in11Hours = baseDateTime.plusHours(11)
        assertThat(in11Hours.toDetailedReminderString()).isEqualTo("Past reminder")
    }

    @Test
    fun `toDetailedReminderString should return Today format for same day more than 12 hours`() {
        val laterToday = baseDateTime.plusHours(13)
        assertThat(laterToday.toDetailedReminderString()).isEqualTo("Today, 23:30")
    }

    @Test
    fun `toDetailedReminderString should return Tomorrow format for next day`() {
        val tomorrowDateTime = baseDateTime.plusDays(1)
        assertThat(tomorrowDateTime.toDetailedReminderString()).isEqualTo("Tomorrow, 10:30")
    }

    @Test
    fun `toDetailedReminderString should return day name for 2-6 days ahead`() {
        val wednesdayDateTime = baseDateTime.plusDays(2)
        assertThat(wednesdayDateTime.toDetailedReminderString()).isEqualTo("Wednesday, 10:30")
    }

    @Test
    fun `toDetailedReminderString should return month day time for 7-30 days ahead`() {
        val in20Days = baseDateTime.plusDays(20) // February 4
        assertThat(in20Days.toDetailedReminderString()).isEqualTo("February 4, 10:30")
    }

    @Test
    fun `toDetailedReminderString should return month day for 31-365 days same year`() {
        val in100Days = baseDateTime.plusDays(100) // April 24
        assertThat(in100Days.toDetailedReminderString()).isEqualTo("April 24")
    }

    @Test
    fun `toDetailedReminderString should return year month day for different year`() {
        val nextYearDateTime = LocalDateTime(2025, 2, 15, 10, 30)
        assertThat(nextYearDateTime.toDetailedReminderString()).isEqualTo("2025/2/15")
    }

    @Test
    fun `toDetailedReminderString should return year month day for more than 365 days`() {
        val farFutureDateTime = baseDateTime.plusDays(400)
        assertThat(farFutureDateTime.toDetailedReminderString()).isEqualTo("2025/2/19")
    }

    @Test
    fun `toDetailedReminderString should handle edge case of exactly 1 minute`() {
        val in1Minute = baseDateTime.plusMinutes(1)
        assertThat(in1Minute.toDetailedReminderString()).isEqualTo("In 1m")
    }

    @Test
    fun `toDetailedReminderString should handle edge case of exactly 1 hour`() {
        val in1Hour = baseDateTime.plusHours(1)
        assertThat(in1Hour.toDetailedReminderString()).isEqualTo("In 1h")
    }

    @Test
    fun `Reminder toReminderString should return basic string for NONE repeat interval`() {
        val reminder = Reminder(
            time = baseDateTime.plusDays(1), repeatInterval = RepeatInterval.NONE, isEnabled = true
        )
        assertThat(reminder.toReminderString()).isEqualTo("Tomorrow, 10:30")
    }

    @Test
    fun `Reminder toReminderString should return daily format`() {
        val reminder = Reminder(
            time = baseDateTime, repeatInterval = RepeatInterval.DAILY, isEnabled = true
        )
        assertThat(reminder.toReminderString()).isEqualTo("Every day, 10:30")
    }

    @Test
    fun `Reminder toReminderString should return weekdays format`() {
        val reminder = Reminder(
            time = baseDateTime, repeatInterval = RepeatInterval.WEEKDAYS, isEnabled = true
        )
        assertThat(reminder.toReminderString()).isEqualTo("Every weekday, 10:30")
    }

    @Test
    fun `Reminder toReminderString should return weekends format`() {
        val reminder = Reminder(
            time = baseDateTime, repeatInterval = RepeatInterval.WEEKENDS, isEnabled = true
        )
        assertThat(reminder.toReminderString()).isEqualTo("Every weekend, 10:30")
    }

    @Test
    fun `Reminder toReminderString should return weekly format with day name`() {
        val reminder = Reminder(
            time = baseDateTime, // Monday
            repeatInterval = RepeatInterval.WEEKLY, isEnabled = true
        )
        assertThat(reminder.toReminderString()).isEqualTo("Every week, Monday 10:30")
    }

    @Test
    fun `Reminder toReminderString should return biweekly format with day name`() {
        val reminder = Reminder(
            time = baseDateTime, // Monday
            repeatInterval = RepeatInterval.BIWEEKLY, isEnabled = true
        )
        assertThat(reminder.toReminderString()).isEqualTo("Every 2 weeks, Monday 10:30")
    }

    @Test
    fun `Reminder toReminderString should return monthly format with ordinal suffixes`() {
        val reminder1st = Reminder(
            time = LocalDateTime(2024, 1, 1, 10, 30),
            repeatInterval = RepeatInterval.MONTHLY,
            isEnabled = true
        )
        assertThat(reminder1st.toReminderString()).isEqualTo("Every month, 1st 10:30")

        val reminder2nd = Reminder(
            time = LocalDateTime(2024, 1, 2, 10, 30),
            repeatInterval = RepeatInterval.MONTHLY,
            isEnabled = true
        )
        assertThat(reminder2nd.toReminderString()).isEqualTo("Every month, 2nd 10:30")

        val reminder3rd = Reminder(
            time = LocalDateTime(2024, 1, 3, 10, 30),
            repeatInterval = RepeatInterval.MONTHLY,
            isEnabled = true
        )
        assertThat(reminder3rd.toReminderString()).isEqualTo("Every month, 3rd 10:30")

        val reminder4th = Reminder(
            time = LocalDateTime(2024, 1, 4, 10, 30),
            repeatInterval = RepeatInterval.MONTHLY,
            isEnabled = true
        )
        assertThat(reminder4th.toReminderString()).isEqualTo("Every month, 4th 10:30")

        val reminder11th = Reminder(
            time = LocalDateTime(2024, 1, 11, 10, 30),
            repeatInterval = RepeatInterval.MONTHLY,
            isEnabled = true
        )
        assertThat(reminder11th.toReminderString()).isEqualTo("Every month, 11th 10:30")

        val reminder21st = Reminder(
            time = LocalDateTime(2024, 1, 21, 10, 30),
            repeatInterval = RepeatInterval.MONTHLY,
            isEnabled = true
        )
        assertThat(reminder21st.toReminderString()).isEqualTo("Every month, 21st 10:30")

        val reminder22nd = Reminder(
            time = LocalDateTime(2024, 1, 22, 10, 30),
            repeatInterval = RepeatInterval.MONTHLY,
            isEnabled = true
        )
        assertThat(reminder22nd.toReminderString()).isEqualTo("Every month, 22nd 10:30")
    }

    @Test
    fun `Reminder toReminderString should return quarterly format with ordinal suffixes`() {
        val reminder = Reminder(
            time = LocalDateTime(2024, 1, 15, 10, 30),
            repeatInterval = RepeatInterval.QUARTERLY,
            isEnabled = true
        )
        assertThat(reminder.toReminderString()).isEqualTo("Every 3 months, 15th 10:30")
    }

    @Test
    fun `Reminder toReminderString should return biannual format`() {
        val reminder = Reminder(
            time = LocalDateTime(2024, 6, 15, 10, 30), // June
            repeatInterval = RepeatInterval.BIANNUALLY, isEnabled = true
        )
        assertThat(reminder.toReminderString()).isEqualTo("Every 6 months, June 15 10:30")
    }

    @Test
    fun `Reminder toReminderString should return yearly format`() {
        val reminder = Reminder(
            time = LocalDateTime(2024, 12, 25, 10, 30), // December
            repeatInterval = RepeatInterval.YEARLY, isEnabled = true
        )
        assertThat(reminder.toReminderString()).isEqualTo("Every year, December 25 10:30")
    }

    @Test
    fun `Reminder toReminderString should handle single digit hours and minutes`() {
        val reminder = Reminder(
            time = LocalDateTime(2024, 1, 15, 9, 5),
            repeatInterval = RepeatInterval.DAILY,
            isEnabled = true
        )
        assertThat(reminder.toReminderString()).isEqualTo("Every day, 09:05")
    }

    @Test
    fun `plusDays with Int should add days correctly`() {
        val result = baseDateTime.plusDays(5, fixedTimeZone)
        val expected = LocalDateTime(2024, 1, 20, 10, 30)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `plusDays with Long should add days correctly`() {
        val result = baseDateTime.plusDays(10L, fixedTimeZone)
        val expected = LocalDateTime(2024, 1, 25, 10, 30)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `plusWeeks should add weeks correctly`() {
        val result = baseDateTime.plusWeeks(2, fixedTimeZone)
        val expected = LocalDateTime(2024, 1, 29, 10, 30)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `plusMonths with Int should add months correctly`() {
        val result = baseDateTime.plusMonths(2, fixedTimeZone)
        val expected = LocalDateTime(2024, 3, 15, 10, 30)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `plusMonths with Long should add months correctly`() {
        val result = baseDateTime.plusMonths(3L, fixedTimeZone)
        val expected = LocalDateTime(2024, 4, 15, 10, 30)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `plusYears with Int should add years correctly`() {
        val result = baseDateTime.plusYears(1, fixedTimeZone)
        val expected = LocalDateTime(2025, 1, 15, 10, 30)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `plusYears with Long should add years correctly`() {
        val result = baseDateTime.plusYears(2L, fixedTimeZone)
        val expected = LocalDateTime(2026, 1, 15, 10, 30)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `minusDays should subtract days correctly`() {
        val result = baseDateTime.minusDays(5L, fixedTimeZone)
        val expected = LocalDateTime(2024, 1, 10, 10, 30)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `plusHours should add hours correctly`() {
        val result = baseDateTime.plusHours(5L, fixedTimeZone)
        val expected = LocalDateTime(2024, 1, 15, 15, 30)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `plusMinutes should add minutes correctly`() {
        val result = baseDateTime.plusMinutes(45L, fixedTimeZone)
        val expected = LocalDateTime(2024, 1, 15, 11, 15)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `withHour should change hour correctly`() {
        val result = baseDateTime.withHour(15)
        val expected = LocalDateTime(2024, 1, 15, 15, 30, 0, 0)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `withMinute should change minute correctly`() {
        val result = baseDateTime.withMinute(45)
        val expected = LocalDateTime(2024, 1, 15, 10, 45, 0, 0)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `withHour should handle edge cases`() {
        val result = baseDateTime.withHour(0)
        assertThat(result.hour).isEqualTo(0)

        val result23 = baseDateTime.withHour(23)
        assertThat(result23.hour).isEqualTo(23)
    }

    @Test
    fun `withMinute should handle edge cases`() {
        val result = baseDateTime.withMinute(0)
        assertThat(result.minute).isEqualTo(0)

        val result59 = baseDateTime.withMinute(59)
        assertThat(result59.minute).isEqualTo(59)
    }

    @Test
    fun `should handle year boundary correctly`() {
        val newYearDateTime = LocalDateTime(2024, 12, 31, 23, 59)
        val nextYear = newYearDateTime.plusDays(1L, fixedTimeZone)
        assertThat(nextYear.year).isEqualTo(2025)
        assertThat(nextYear.monthNumber).isEqualTo(1)
        assertThat(nextYear.dayOfMonth).isEqualTo(1)
    }

    @Test
    fun `should handle leap year correctly`() {
        val leapYearFeb = LocalDateTime(2024, 2, 28, 10, 30)
        val nextDay = leapYearFeb.plusDays(1L, fixedTimeZone)
        assertThat(nextDay.dayOfMonth).isEqualTo(29) // Leap day

        val afterLeapDay = nextDay.plusDays(1L, fixedTimeZone)
        assertThat(afterLeapDay.monthNumber).isEqualTo(3)
        assertThat(afterLeapDay.dayOfMonth).isEqualTo(1)
    }

    @Test
    fun `should handle different time zones when using default parameter`() {
        val result = baseDateTime.plusDays(1)
        assertThat(result.dayOfMonth).isEqualTo(16)
    }

    @Test
    fun `toReminderString should handle all day of week values`() {
        val daysOfWeek = listOf(
            DayOfWeek.MONDAY to "Monday",
            DayOfWeek.TUESDAY to "Tuesday",
            DayOfWeek.WEDNESDAY to "Wednesday",
            DayOfWeek.THURSDAY to "Thursday",
            DayOfWeek.FRIDAY to "Friday",
            DayOfWeek.SATURDAY to "Saturday",
            DayOfWeek.SUNDAY to "Sunday"
        )

        daysOfWeek.forEach { (dayOfWeek, expectedName) ->
            val currentDay = baseDateTime.dayOfWeek
            val daysToAdd = if (dayOfWeek.ordinal >= currentDay.ordinal) {
                dayOfWeek.ordinal - currentDay.ordinal + 2
            } else {
                7 - currentDay.ordinal + dayOfWeek.ordinal + 2
            }

            val futureDateTime = baseDateTime.plusDays(daysToAdd.toLong())
            assertThat(futureDateTime.toReminderString()).contains(expectedName)
        }
    }

    @Test
    fun `toReminderString should handle all months correctly`() {
        val months = Month.entries
        months.forEach { month ->
            val dateTime = LocalDateTime(2024, month, 15, 10, 30)
            val reminder = Reminder(
                time = dateTime, repeatInterval = RepeatInterval.YEARLY, isEnabled = true
            )
            val monthName = month.name.lowercase().replaceFirstChar { it.uppercase() }
            assertThat(reminder.toReminderString()).contains(monthName)
        }
    }
}