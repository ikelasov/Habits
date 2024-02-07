package com.example.habits.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StatisticsRepository @Inject constructor() {
    fun getStatistics(): Flow<StatisticsData> {
        return flow {
            emit(
                StatisticsData(
                    longestStreakData = StatisticsTypeData(StatisticsType.LONGEST_STREAK, 20),
                    currentStreakData = StatisticsTypeData(StatisticsType.CURRENT_STREAK, 7),
                    completionRate = StatisticsTypeData(StatisticsType.COMPLETION_RATE, 98),
                    averageTasks = StatisticsTypeData(StatisticsType.AVERAGE_TASKS, 7),
                )
            )
        }
    }
}

data class StatisticsData(
    val longestStreakData: StatisticsTypeData,
    val currentStreakData: StatisticsTypeData,
    val completionRate: StatisticsTypeData,
    val averageTasks: StatisticsTypeData
)

data class StatisticsTypeData(
    val type: StatisticsType,
    val value: Int
)

enum class StatisticsType(val hint: String) {
    LONGEST_STREAK("Longest streak"),
    CURRENT_STREAK("Current streak"),
    COMPLETION_RATE("Completion rate"),
    AVERAGE_TASKS("Average tasks")
}