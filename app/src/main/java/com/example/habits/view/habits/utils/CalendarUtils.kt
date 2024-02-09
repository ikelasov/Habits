package com.example.habits.view.habits.utils

import com.example.habits.view.habits.CalendarItemUi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.formatMonthYear(): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
    return this.format(formatter)
}

fun getDaysOfMonthAbbreviated(
    year: Int,
    month: Int,
    selectedDay: LocalDate,
): List<CalendarItemUi> {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1)

    return (1..lastDayOfMonth.dayOfMonth).map { dayOfMonth ->
        val dayDate = LocalDate.of(year, month, dayOfMonth)
        val dayOfWeek =
            dayDate.format(DateTimeFormatter.ofPattern("E", Locale.getDefault())).first()
                .toString()
        CalendarItemUi(
            dayOfWeekIndication = dayOfWeek,
            dayInMonthIndication = dayOfMonth,
            isSelected = dayDate == selectedDay,
        )
    }
}
