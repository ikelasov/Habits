package com.example.habits.view.habits.screencomponents

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habits.R
import com.example.habits.view.habits.StatisticsDataUi
import com.example.habits.view.habits.getMockStatisticsDate

@Composable
fun StatisticsContent(statistics: StatisticsDataUi) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = Color.White,
        modifier = Modifier.padding(horizontal = 24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.weight(0.5f)) {
                StatisticsItem(
                    statisticTitle = statistics.longestStreak.title,
                    statisticsComment = statistics.longestStreak.hint,
                    icon = statistics.longestStreak.icon,
                )
                StatisticsItem(
                    statisticTitle = statistics.completionRate.title,
                    statisticsComment = statistics.completionRate.hint,
                    icon = statistics.completionRate.icon,
                )
            }
            Column(modifier = Modifier.weight(0.5f)) {
                StatisticsItem(
                    statisticTitle = statistics.currentStreak.title,
                    statisticsComment = statistics.currentStreak.hint,
                    icon = statistics.currentStreak.icon,
                )
                StatisticsItem(
                    statisticTitle = statistics.averageTasks.title,
                    statisticsComment = statistics.averageTasks.hint,
                    icon = statistics.averageTasks.icon,
                )
            }
        }
    }
}

@Composable
fun StatisticsItem(
    statisticTitle: String,
    statisticsComment: String,
    @DrawableRes icon: Int,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = statisticTitle, style = MaterialTheme.typography.titleLarge)
            Text(
                text = statisticsComment,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
        )
    }
}

@Preview
@Composable
fun StatisticsContentPreview() {
    StatisticsContent(getMockStatisticsDate())
}

@Preview(showBackground = true)
@Composable
fun StatisticsItemPreview() {
    StatisticsItem(
        statisticTitle = "20 Days",
        statisticsComment = "Longest streak",
        icon = R.drawable.ic_longest_streak,
    )
}
