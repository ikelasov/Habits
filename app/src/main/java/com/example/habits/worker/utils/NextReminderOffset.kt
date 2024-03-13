package com.example.habits.worker.utils

import com.example.habits.data.localdatasource.habits.DaysOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

fun findNextRemindersOffsetFromNow(
    daysToRepeat: List<DaysOfWeek>,
    reminderTimes: List<LocalTime>,
): Long {
    val currentDateTime = LocalDateTime.now()
    val currentTime = currentDateTime.toLocalTime()
    val currentDayOfWeek = currentDateTime.dayOfWeek
    val repetitionDaysToOffsetFromTodayPair =
        daysToRepeat.map {
            it to it.ordinal - currentDayOfWeek.ordinal
        }

    return when {
        thereAreRemindersForTodayAfterNow(
            repetitionDaysToOffsetFromTodayPair,
            reminderTimes,
            currentTime,
        ) -> findNextReminderOffsetForToday(reminderTimes, currentDateTime)

        thereAreRemindersForThisWeek(repetitionDaysToOffsetFromTodayPair) ->
            findNextReminderOffsetForThisWeek(
                repetitionDaysToOffsetFromTodayPair,
                reminderTimes,
                currentDateTime,
            )

        else ->
            findNextReminderOffsetForNextWeek(
                repetitionDaysToOffsetFromTodayPair,
                currentDateTime,
                reminderTimes,
            )
    }
}

// region Today

private fun thereAreRemindersForTodayAfterNow(
    repetitionDaysToOffsetFromTodayPair: List<Pair<DaysOfWeek, Int>>,
    reminderTimes: List<LocalTime>,
    currentTimeNow: LocalTime,
): Boolean {
    val areThereRepetitionsToday = repetitionDaysToOffsetFromTodayPair.any { it.second == 0 }
    val areThereRepetitionsAfterNow = reminderTimes.any { it.isAfter(currentTimeNow) }

    return areThereRepetitionsToday && areThereRepetitionsAfterNow
}

private fun findNextReminderOffsetForToday(
    reminderTimes: List<LocalTime>,
    currentDateTimeNow: LocalDateTime,
): Long {
    val nextReminder =
        getEarliestReminderForTodayAfterNow(reminderTimes, currentDateTimeNow)

    return ChronoUnit.SECONDS.between(currentDateTimeNow, nextReminder)
}

private fun getEarliestReminderForTodayAfterNow(
    reminderTimes: List<LocalTime>,
    currentDateTimeNow: LocalDateTime,
): LocalDateTime {
    val timesAfterNow = reminderTimes.filter { it.isAfter(currentDateTimeNow.toLocalTime()) }
    val earliestTime = timesAfterNow.map { it.toSecondOfDay() }.minByOrNull { it }!!
    return currentDateTimeNow.toLocalDate().atStartOfDay().plusSeconds(earliestTime.toLong())
}

// endregion

// region This Week

private fun thereAreRemindersForThisWeek(repetitionDaysToOffsetFromTodayPair: List<Pair<DaysOfWeek, Int>>): Boolean {
    return repetitionDaysToOffsetFromTodayPair.any { it.second > 0 }
}

private fun findNextReminderOffsetForThisWeek(
    repetitionDaysToOffsetFromTodayPair: List<Pair<DaysOfWeek, Int>>,
    reminderTimes: List<LocalTime>,
    currentDateTimeNow: LocalDateTime,
): Long {
    val repetitionsAfterToday =
        repetitionDaysToOffsetFromTodayPair.filter { it.second > 0 }
    val offsetOfTheClosestDay =
        repetitionsAfterToday
            .minByOrNull { it.second }!!
            .second
    val earliestReminderTime =
        reminderTimes.minByOrNull { it.toSecondOfDay() }!!

    val targetDateTime =
        currentDateTimeNow
            .toLocalDate()
            .plusDays(offsetOfTheClosestDay.toLong())
            .atStartOfDay()
            .plusSeconds(earliestReminderTime.toSecondOfDay().toLong())

    return ChronoUnit.SECONDS.between(currentDateTimeNow, targetDateTime)
}

// endregion

// region Next Week

private fun findNextReminderOffsetForNextWeek(
    repetitionDaysToOffsetFromTodayPair: List<Pair<DaysOfWeek, Int>>,
    currentDateTimeNow: LocalDateTime,
    reminderTimes: List<LocalTime>,
): Long {
    val repetitionsBeforeToday =
        repetitionDaysToOffsetFromTodayPair.filter { it.second < 0 }
    val offsetOfTheClosestDay =
        repetitionsBeforeToday.maxByOrNull { it.second }!!.second + 7
    // Since the offset of the reminder day is negative (because it belongs to previous days)
    // we add 7 days to calculate the offset for the same day of the next week
    val adjustedOffset = offsetOfTheClosestDay + 7
    val earliestReminderTime =
        reminderTimes.minByOrNull { it.toSecondOfDay() }!!

    val targetDateTime =
        currentDateTimeNow
            .toLocalDate()
            .plusDays((adjustedOffset).toLong())
            .atStartOfDay()
            .plusSeconds(earliestReminderTime.toSecondOfDay().toLong())

    return ChronoUnit.SECONDS.between(currentDateTimeNow, targetDateTime)
}

// endregion
