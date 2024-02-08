package com.example.habits.view.habitsscreen.utils

import com.example.habits.view.habitsscreen.CalendarItemUi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun getDaysOfMonth(
    year: Int,
    month: Int,
): List<CalendarItemUi> {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1)

    val days = mutableListOf<Pair<String, Int>>()
    var currentDate = firstDayOfMonth

    while (!currentDate.isAfter(lastDayOfMonth)) {
        val dayOfWeek =
            currentDate.format(DateTimeFormatter.ofPattern("E", Locale.getDefault())).first()
                .toString()
        val dayOfMonth = currentDate.dayOfMonth
        days.add(Pair(dayOfWeek, dayOfMonth))
        currentDate = currentDate.plusDays(1)
    }

    return days.map {
        CalendarItemUi(
            dayOfWeekIndication = it.first,
            dayInMonthIndication = it.second,
            false,
        )
    }
}
