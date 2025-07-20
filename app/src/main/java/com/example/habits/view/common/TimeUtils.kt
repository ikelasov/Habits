package com.example.habits.view.common

import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun LocalTime.formatAsHHmm(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return this.format(formatter)
}

fun toLocalTime(hour: Int, minute: Int): LocalTime {
    return LocalTime.of(hour, minute)
}