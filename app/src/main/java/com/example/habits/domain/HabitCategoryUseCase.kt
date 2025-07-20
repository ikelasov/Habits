package com.example.habits.domain

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

    suspend fun createCategory(categoryName: String) {
        if (categoryName.isBlank()) return

        val newCategory = HabitCategoryEntity(name = categoryName)
        habitsRepository.insertCategory(newCategory)
    }
}