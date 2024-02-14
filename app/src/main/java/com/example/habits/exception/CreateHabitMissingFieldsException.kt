package com.example.habits.exception

class CreateHabitMissingFieldsException(
    val listOfMissingFields: List<CreateHabitMissingFields>,
) : Exception()

enum class CreateHabitMissingFields(val value: String) {
    HABIT_NAME("Habit name"),
    DAYS_TO_REPEAT("Days to repeat"),
}
