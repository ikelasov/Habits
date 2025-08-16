package com.example.habits.feature_habits.createhabit.domain

import com.example.habits.core.common.model.DaysOfWeek
import com.example.habits.feature_habits.createhabit.exception.CreateHabitMissingFields
import com.example.habits.feature_habits.createhabit.exception.CreateHabitMissingFieldsException

internal fun throwIfMissingFields(
    habitName: String,
    daysToRepeat: List<DaysOfWeek>,
    categoryId: String?,
) {
    val missingFields = mutableListOf<CreateHabitMissingFields>()
    if (habitName.isEmpty()) {
        missingFields.add(CreateHabitMissingFields.HABIT_NAME)
    }
    if (daysToRepeat.isEmpty()) {
        missingFields.add(CreateHabitMissingFields.DAYS_TO_REPEAT)
    }
    if (categoryId == null) {
        missingFields.add(CreateHabitMissingFields.HABIT_CATEGORY)
    }

    if (missingFields.isNotEmpty()) {
        throw CreateHabitMissingFieldsException(missingFields)
    }
}