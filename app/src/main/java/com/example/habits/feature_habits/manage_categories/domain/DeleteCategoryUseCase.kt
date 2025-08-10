package com.example.habits.feature_habits.manage_categories.domain

import com.example.habits.core.data.habitcategories.repository.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categoryId: String) =
        categoryRepository.deleteCategory(categoryId)
}