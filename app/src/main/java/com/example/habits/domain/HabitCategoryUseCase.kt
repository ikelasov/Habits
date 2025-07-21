package com.example.habits.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.habits.data.localdatasource.habitscategory.HabitCategoryEntity
import com.example.habits.data.repository.HabitsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HabitCategoryUseCase @Inject constructor(
    private val habitsRepository: HabitsRepository
) {

    fun getCategoriesFlow(): Flow<List<HabitCategoryEntity>> {
        return habitsRepository.getCategories()
    }

    suspend fun createCategory(categoryName: String, color: Color) {
        if (categoryName.isBlank()) return

        val newCategory = HabitCategoryEntity(name = categoryName, color = color.toArgb())
        habitsRepository.insertCategory(newCategory)
    }
}