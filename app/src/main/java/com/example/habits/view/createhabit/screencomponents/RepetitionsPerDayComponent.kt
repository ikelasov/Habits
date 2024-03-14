package com.example.habits.view.createhabit.screencomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habits.R
import com.example.habits.ui.theme.HabitsTheme

@Composable
fun RepetitionsPerDayComponent(
    repetitionsPerDay: Int,
    onRepetitionsNumberPerDayChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconButtonsBackgroundColor = colorResource(R.color.black)
    Surface(
        shape = MaterialTheme.shapes.large,
        modifier = modifier,
        color = Color.White,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.times_per_day),
                modifier =
                Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 16.dp),
            ) {
                IconButton(
                    onClick = {
                        onRepetitionsNumberPerDayChanged(repetitionsPerDay - 1)
                    },
                    enabled = repetitionsPerDay > 1,
                ) {
                    Image(
                        painterResource(
                            if (repetitionsPerDay > 1)
                                R.drawable.minus_with_background
                            else
                                R.drawable.minus_with_background_disabled
                        ),
                        contentDescription = null,
                    )
                }
                Text(
                    text = repetitionsPerDay.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
                IconButton(
                    onClick = {
                        onRepetitionsNumberPerDayChanged(repetitionsPerDay + 1)
                    },
                ) {
                    Image(
                        painterResource(R.drawable.plus_with_background),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TaskRepetitionPreview() {
    HabitsTheme {
        RepetitionsPerDayComponent(1, {})
    }
}
