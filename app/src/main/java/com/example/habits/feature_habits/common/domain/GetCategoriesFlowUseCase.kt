package com.example.habits.feature_habits.common.domain

import com.example.habits.core.data.habitcategories.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesFlowUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
) {
    operator fun invoke() = categoryRepository.getCategoriesForUserFlow()
}