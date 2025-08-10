package com.example.habits.feature_habits.createhabit.domain

import com.example.habits.core.data.habitcategories.repository.CategoryRepository
import javax.inject.Inject

class CreateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
) {
    suspend operator fun invoke(categoryName: String, colorHex: String) {
        categoryRepository.createCategory(categoryName, colorHex)
    }
}