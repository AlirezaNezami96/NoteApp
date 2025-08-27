package alireza.nezami.common.mock

import alireza.nezami.model.domain.Note
import alireza.nezami.model.domain.Reminder
import alireza.nezami.model.domain.RepeatInterval
import java.time.LocalDateTime
import java.util.Random

/**
 * Mock data provider for Note objects
 */
object NoteMockProvider {

    private val sampleTitles = listOf(
        "Meeting with team",
        "Buy groceries",
        "Call mom",
        "Workout session",
        "Doctor appointment",
        "Project deadline",
        "Book club meeting",
        "Pay bills",
        "Car maintenance",
        "Birthday party",
        "Vacation planning",
        "Learn Kotlin",
        "Write blog post",
        "Clean house",
        "Date night",
        "Job interview",
        "Dentist checkup",
        "Gift shopping",
        "Movie night",
        "Study session"
    )

    private val sampleContents = listOf(
        "Discuss project milestones and next steps for the upcoming sprint",
        "Need to buy milk, bread, eggs, and vegetables for the week",
        "Check in with mom about her recent doctor visit and weekend plans",
        "45-minute cardio session followed by strength training at the gym",
        "Annual health checkup with Dr. Smith at 2 PM downtown",
        "Final review and submission of the quarterly report by EOD",
        "Monthly discussion of 'The Great Gatsby' at Sarah's house",
        "Pay electricity, water, and internet bills before due date",
        "Oil change and tire rotation scheduled at the service center",
        "Jake's 30th birthday celebration at the restaurant on Main Street",
        "Research destinations and book flights for summer vacation",
        "Continue with advanced Kotlin concepts and coroutines tutorial",
        "Write about best practices in Android development for the company blog",
        "Deep clean the kitchen and organize the storage closet",
        "Dinner and movie with partner at the new Italian restaurant",
        "Technical interview for Senior Android Developer position",
        "Routine cleaning and checkup, remember to bring insurance card",
        "Find presents for nephew's graduation and sister's promotion",
        "Watch the new Marvel movie with friends, buy tickets in advance",
        "Review materials for the certification exam next week"
    )

    private val sampleLabels = listOf(
        listOf("work", "important"),
        listOf("personal", "shopping"),
        listOf("family"),
        listOf("health", "fitness"),
        listOf("health", "appointment"),
        listOf("work", "deadline"),
        listOf("social", "books"),
        listOf("finance", "bills"),
        listOf("car", "maintenance"),
        listOf("social", "celebration"),
        listOf("travel", "planning"),
        listOf("learning", "programming"),
        listOf("work", "writing"),
        listOf("home", "cleaning"),
        listOf("personal", "relationship"),
        listOf("work", "interview"),
        listOf("health", "dental"),
        listOf("shopping", "gifts"),
        listOf("entertainment", "movies"),
        listOf("education", "study")
    )

    /**
     * Generates a list of mock Note objects with varied reminder times
     */
    fun provideMockNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val baseTime = LocalDateTime.now()
        val random = Random()

        repeat(20) { index ->
            val createdAt = baseTime.minusDays(random.nextInt(30).toLong())
            val updatedAt = createdAt.plusDays(random.nextInt(7).toLong())

            // Create varied reminder times for demonstration
            val reminder = when (index) { // Today reminders
                0 -> Reminder(
                    time = baseTime.plusHours(2),
                    isEnabled = true,
                    repeatInterval = RepeatInterval.NONE
                )

                1 -> Reminder(
                    time = baseTime.plusHours(5),
                    isEnabled = true,
                    repeatInterval = RepeatInterval.DAILY
                ) // Tomorrow reminders
                2 -> Reminder(
                    time = baseTime.plusDays(1).withHour(9).withMinute(0),
                    isEnabled = true,
                    repeatInterval = RepeatInterval.NONE
                )

                3 -> Reminder(
                    time = baseTime.plusDays(1).withHour(18).withMinute(30),
                    isEnabled = false,
                    repeatInterval = RepeatInterval.WEEKLY
                ) // This week reminders
                4, 5, 6 -> Reminder(
                    time = baseTime.plusDays((index - 2).toLong()).withHour(10 + index)
                        .withMinute(0),
                    isEnabled = true,
                    repeatInterval = RepeatInterval.entries[random.nextInt(RepeatInterval.entries.size)]
                ) // This month reminders
                7, 8, 9, 10 -> Reminder(
                    time = baseTime.plusDays((7 + index).toLong()).withHour(random.nextInt(12) + 8)
                        .withMinute(random.nextInt(60)),
                    isEnabled = random.nextBoolean(),
                    repeatInterval = RepeatInterval.entries[random.nextInt(RepeatInterval.entries.size)]
                ) // Future reminders (same year)
                11, 12, 13, 14 -> Reminder(
                    time = baseTime.plusMonths((index - 10).toLong())
                        .withHour(random.nextInt(12) + 8).withMinute(0),
                    isEnabled = true,
                    repeatInterval = RepeatInterval.entries[random.nextInt(RepeatInterval.entries.size)]
                ) // Next year reminders
                15, 16, 17 -> Reminder(
                    time = baseTime.plusYears(1).plusMonths((index - 15).toLong())
                        .withHour(random.nextInt(12) + 8).withMinute(0),
                    isEnabled = random.nextBoolean(),
                    repeatInterval = RepeatInterval.entries[random.nextInt(RepeatInterval.entries.size)]
                ) // Some notes without reminders
                else -> null
            }

            notes.add(
                Note(
                    id = (index + 1).toLong(),
                    title = sampleTitles[index],
                    content = sampleContents[index],
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    reminder = reminder,
                    labels = sampleLabels[index]
                )
            )
        }

        return notes
    }

    /**
     * Provides a specific number of mock notes
     */
    fun provideMockNotes(count: Int): List<Note> {
        return provideMockNotes().take(count)
    }

    /**
     * Provides mock notes with only active reminders
     */
    fun provideMockNotesWithActiveReminders(): List<Note> {
        return provideMockNotes().filter { it.reminder?.isEnabled == true }
    }
}