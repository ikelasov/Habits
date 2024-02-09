package com.example.habits.view.habits.mapper

import com.example.habits.R
import com.example.habits.data.repository.StatisticsData
import com.example.habits.data.repository.StatisticsType
import com.example.habits.data.repository.StatisticsTypeData
import com.example.habits.view.habits.StatisticsDataUi
import com.example.habits.view.habits.StatisticsItemUi

fun StatisticsData.mapToStatisticsDataUi(): StatisticsDataUi {
    val longestStreakTitle = this.longestStreakData.resolveStatisticsTitle()
    val currentStreakTitle = this.currentStreakData.resolveStatisticsTitle()
    val completionRateTitle = this.completionRate.resolveStatisticsTitle()
    val averageTasksTitle = this.averageTasks.resolveStatisticsTitle()

    val longestStreakIcon = this.longestStreakData.resolveStatisticsIconResource()
    val currentStreakIcon = this.currentStreakData.resolveStatisticsIconResource()
    val completionRateIcon = this.completionRate.resolveStatisticsIconResource()
    val averageTasksIcon = this.averageTasks.resolveStatisticsIconResource()

    return StatisticsDataUi(
        longestStreak =
            StatisticsItemUi(
                longestStreakTitle,
                this.longestStreakData.type.hint,
                longestStreakIcon,
            ),
        averageTasks =
            StatisticsItemUi(
                averageTasksTitle,
                this.averageTasks.type.hint,
                averageTasksIcon,
            ),
        completionRate =
            StatisticsItemUi(
                completionRateTitle,
                this.completionRate.type.hint,
                completionRateIcon,
            ),
        currentStreak =
            StatisticsItemUi(
                currentStreakTitle,
                this.currentStreakData.type.hint,
                currentStreakIcon,
            ),
    )
}

private fun StatisticsTypeData.resolveStatisticsTitle(): String {
    val shouldUsePlural = this.value == 1
    return when (this.type) {
        StatisticsType.LONGEST_STREAK -> "${this.value} Days" + if (shouldUsePlural) "s" else ""
        StatisticsType.CURRENT_STREAK -> "${this.value} Days" + if (shouldUsePlural) "s" else ""
        StatisticsType.COMPLETION_RATE -> "${this.value}%"
        StatisticsType.AVERAGE_TASKS -> this.value.toString()
    }
}

private fun StatisticsTypeData.resolveStatisticsIconResource(): Int {
    return when (this.type) {
        StatisticsType.LONGEST_STREAK -> R.drawable.ic_longest_streak
        StatisticsType.CURRENT_STREAK -> R.drawable.ic_current_streak
        StatisticsType.COMPLETION_RATE -> R.drawable.ic_completion_rate
        StatisticsType.AVERAGE_TASKS -> R.drawable.ic_average_tasks
    }
}
