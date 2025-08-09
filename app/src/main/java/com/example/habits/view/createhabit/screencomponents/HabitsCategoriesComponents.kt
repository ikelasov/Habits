package com.example.habits.view.createhabit.screencomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.habits.R
import com.example.habits.data.habitscategory.localdatasource.HabitCategoryEntity
import com.example.habits.ui.theme.HabitsTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HabitsCategoriesComponent(
    allCategories: List<HabitCategoryEntity>,
    selectedCategory: HabitCategoryEntity?,
    onCategorySelected: (HabitCategoryEntity) -> Unit,
    onAddNewClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Category")
            Spacer(modifier = Modifier.height(16.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
            ) {
                allCategories.forEach { category ->
                    CategoryChip(
                        text = category.name,
                        categoryColor = Color(category.color),
                        isSelected = category.id == selectedCategory?.id,
                        onClick = { onCategorySelected(category) }
                    )
                }

                AddCategoryChip(onClick = onAddNewClicked)
            }
        }
    }
}

@Composable
fun CategoryChip(
    text: String,
    categoryColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        shape = CircleShape,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) categoryColor else colorResource(R.color.light_gray)
        ),
        color = Color.Transparent
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun AddCategoryChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        shape = CircleShape,
        border = BorderStroke(
            width = 1.dp,
            color = colorResource(R.color.light_gray)
        ),
        color = Color.Transparent
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add new category",
                modifier = Modifier.size(18.dp)
            )
            Text("New category")
        }
    }
}

val categories = listOf(
    HabitCategoryEntity("1", "Fitness", Color.Red.toArgb(), userId = "1", createdAt = 123456789),
    HabitCategoryEntity("2", "Work", Color.Yellow.toArgb(), userId = "1", createdAt = 123456789),
    HabitCategoryEntity("3", "Reading", Color.Green.toArgb(), userId = "1", createdAt = 123456789),
    HabitCategoryEntity("4", "Health", Color.Blue.toArgb(), userId = "1", createdAt = 123456789)
)

@PreviewLightDark
@Composable
private fun HabitsCategoriesComponentPreview() {
    var selectedCategory by remember { mutableStateOf(categories[1]) }

    HabitsTheme {
        HabitsCategoriesComponent(
            allCategories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { category -> selectedCategory = category },
            onAddNewClicked = { /* TODO: Handle Add click in preview if needed */ },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun CategoryChipPreview() {
    HabitsTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryChip(
                text = "Selected",
                categoryColor = Color.Green,
                isSelected = true,
                onClick = {})
            CategoryChip(
                text = "Normal",
                categoryColor = Color.Cyan,
                isSelected = false,
                onClick = {})
            AddCategoryChip(onClick = {})
        }
    }
}