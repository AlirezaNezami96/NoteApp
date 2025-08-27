package alireza.nezami.model.domain

enum class RepeatInterval {
    NONE,
    DAILY,
    WEEKDAYS,
    WEEKENDS,
    WEEKLY,
    BIWEEKLY,
    MONTHLY,
    QUARTERLY,  // Every 3 months
    BIANNUALLY, // Every 6 months
    YEARLY
}