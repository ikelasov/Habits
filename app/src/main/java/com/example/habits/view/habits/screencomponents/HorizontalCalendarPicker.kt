package com.example.habits.view.habits.screencomponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habits.R
import com.example.habits.ui.theme.HabitsTheme
import com.example.habits.view.habits.CalendarItemUi
import com.example.habits.view.habits.utils.getDaysOfMonth

@Composable
fun HorizontalCalendar(
    selectedMonth: String,
    onNextMonthClicked: () -> Unit,
    onPreviousMonthClicked: () -> Unit,
    onCurrentDateClicked: () -> Unit,
    daysOfMonth: List<CalendarItemUi>,
    onDayClicked: (Int) -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        CalendarMonthSection(
            selectedMonth,
            onNextMonthClicked,
            onPreviousMonthClicked,
            onCurrentDateClicked,
        )
        DaysOfMonthSection(daysOfMonth, selectedMonth, onDayClicked)
    }
}

@Composable
private fun CalendarMonthSection(
    selectedMonth: String,
    onNextMonthClicked: () -> Unit,
    onPreviousMonthClicked: () -> Unit,
    onCurrentDateClicked: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = { onPreviousMonthClicked() },
        ) {
            Icon(
                Icons.Default.KeyboardArrowLeft,
                contentDescription = null,
            )
        }
        Text(
            text = selectedMonth,
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp)
                    .clickable { onCurrentDateClicked() },
            textAlign = TextAlign.Center,
        )
        IconButton(onClick = { onNextMonthClicked() }) {
            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun DaysOfMonthSection(
    daysOfMonth: List<CalendarItemUi>,
    selectedMonth: String,
    onDayClicked: (Int) -> Unit,
) {
    val state = rememberLazyListState()
    LaunchedEffect(selectedMonth) {
        val selectedIndex = daysOfMonth.indexOfFirst { it.isSelected }
        if (selectedIndex == -1) {
            state.scrollToItem(0)
        } else {
            state.animateScrollToItem(selectedIndex)
        }
    }
    LazyRow(
        state = state,
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(daysOfMonth) { item ->
            CalendarItem(
                dayNameIndication = item.dayOfWeekIndication,
                dayInMonth = item.dayInMonthIndication,
                isSelected = item.isSelected,
                onDayClicked,
            )
        }
    }
}

@Composable
fun CalendarItem(
    dayNameIndication: String,
    dayInMonth: Int,
    isSelected: Boolean,
    onDayClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) colorResource(R.color.orange) else Color.Transparent,
        animationSpec = tween(),
        label = "",
    )

    Column(
        modifier =
            modifier
                .clickable {
                    onDayClicked(dayInMonth)
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = dayNameIndication,
            fontSize = 14.sp,
            color = if (isSelected) Color.White else colorResource(R.color.slate_blue_gray),
            modifier =
                Modifier
                    .drawBehind {
                        drawCircle(
                            color = backgroundColor,
                        )
                    }
                    .padding(4.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = dayInMonth.toString(), fontSize = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarTimelinePreview() {
    HabitsTheme {
        HorizontalCalendar(
            selectedMonth = "January 2024",
            {},
            {},
            {},
            daysOfMonth = getDaysOfMonth(2024, 3),
            {},
            Modifier.padding(vertical = 16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarItemPreview() {
    HabitsTheme {
        CalendarItem(dayNameIndication = "M", dayInMonth = 24, isSelected = false, {})
    }
}
