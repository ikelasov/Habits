@file:OptIn(ExperimentalFoundationApi::class)

package com.example.habits.view.habits.screencomponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.habits.R
import com.example.habits.ui.theme.HabitsTheme
import com.example.habits.view.habits.DraggedDirection
import com.example.habits.view.habits.HabitUi
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun HabitItem(
    habit: HabitUi,
    onHabitItemDragged: (String, DraggedDirection) -> Unit,
    modifier: Modifier = Modifier,
) {
    var offsetX by remember { mutableStateOf(0f) }
    var componentWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier =
            modifier
                .padding(vertical = 8.dp, horizontal = 24.dp)
                .fillMaxWidth()
                .offset {
                    IntOffset(
                        x = offsetX.roundToInt(),
                        y = 0,
                    )
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX > 0) {
                                onHabitItemDragged(habit.id, DraggedDirection.StartToEnd)
                            } else {
                                onHabitItemDragged(habit.id, DraggedDirection.EndToStart)
                            }
                            offsetX = 0f
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            if ((offsetX + dragAmount).absoluteValue.toDp() <= componentWidth / 2) {
                                offsetX += dragAmount
                                change.consume()
                            }
                        },
                    )
                }
                .onPlaced {
                    with(density) {
                        componentWidth = it.size.width.toDp()
                    }
                },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            HabitPriorityIndication(habit = habit)
            HabitMainInfo(
                habitName = habit.name,
                categoryName = habit.category,
                categoryColor = habit.categoryColor,
                timeToDoIndication = habit.timeToDoIndication,
                daysToRepeat = habit.daysToRepeat,
                repetitionIndication = habit.repetitionIndication,
                modifier = Modifier.weight(1f),
            )
            HabitProgressIndicator(
                progress = habit.progress,
                modifier = Modifier.align(Alignment.CenterVertically),
            )
        }
    }
}

@Composable
private fun HabitPriorityIndication(habit: HabitUi) {
    Surface(
        modifier =
            Modifier
                .padding(end = 12.dp)
                .width(16.dp)
                .fillMaxHeight(),
        color = colorResource(habit.priorityIndicationColor),
    ) {}
}

@Composable
private fun HabitMainInfo(
    habitName: String,
    categoryName: String,
    categoryColor: Color?,
    timeToDoIndication: String,
    daysToRepeat: String,
    repetitionIndication: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = timeToDoIndication,
                style = MaterialTheme.typography.bodySmall,
            )
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Habit repetition icon",
                tint = colorResource(R.color.orange),
                modifier =
                    Modifier
                        .padding(start = 8.dp, end = 2.dp)
                        .size(10.dp),
            )
            Text(
                text = daysToRepeat,
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.orange),
            )
        }
        Text(
            text = habitName,
            style = MaterialTheme.typography.titleLarge,
        )
        if (categoryName.isNotBlank() && categoryColor != null) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .border(
                        width = 1.dp,
                        color = categoryColor,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier =
                Modifier
                    .padding(top = 8.dp, bottom = 8.dp),
        ) {
            Text(
                text = repetitionIndication,
                style = MaterialTheme.typography.labelSmall,
                modifier =
                    Modifier
                        .padding(horizontal = 4.dp)
                        .paddingFromBaseline(top = 8.dp, bottom = 4.dp),
            )
        }
    }
}

@Composable
private fun HabitProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val isCompleted = progress >= 1f
    val completedColor = remember { Color(0xFFA5D6A7) }
    val animatedProgressColor by animateColorAsState(
        targetValue = if (isCompleted) completedColor else MaterialTheme.colorScheme.tertiary,
        animationSpec = tween(500),
        label = "progressColor"
    )
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "progressValue",
        animationSpec = tween(500)
    )

    val checkmarkScale by animateFloatAsState(
        targetValue = if (isCompleted) 1f else 0f,
        animationSpec = tween(durationMillis = 500, delayMillis = 200),
        label = "checkmarkScale"
    )
    val checkmarkAlpha by animateFloatAsState(
        targetValue = if (isCompleted) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = 200),
        label = "checkmarkAlpha"
    )

    Box(
        modifier =
            modifier
                .padding(end = 24.dp)
                .size(40.dp),
    ) {
        CircularProgressIndicator(
            progress = { 1f },
            color = MaterialTheme.colorScheme.surfaceVariant,
        )
        CircularProgressIndicator(
            progress = { animatedProgress },
            color = animatedProgressColor,
        )
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Completed",
            tint = completedColor,
            modifier = Modifier
                .scale(checkmarkScale)
                .alpha(checkmarkAlpha)
                .align(Alignment.Center)
        )
    }
}

@PreviewLightDark
@Composable
fun HabitItemPreview() {
    val mockHabit =
        HabitUi(
            id = "1",
            name = "Go to the gym",
            category = "Category",
            categoryColor = Color.Red,
            timeToDoIndication = "10:00 AM",
            daysToRepeat = "Mon,Sun",
            repetitionIndication = "10 times per day",
            0.3f,
            R.color.purple_200,
        )
    HabitsTheme {
        HabitItem(mockHabit, { _, _ -> })
    }
}
