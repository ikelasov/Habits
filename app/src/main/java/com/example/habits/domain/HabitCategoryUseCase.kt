package com.example.habits.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.habits.data.habitcategories.repository.CategoryRepository
import com.example.habits.data.model.habitcategory.HabitCategoryEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HabitCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    firebaseAuth: FirebaseAuth
) {

    private val userId = firebaseAuth.currentUser?.uid

    fun getCategoriesFlow(): Flow<List<HabitCategoryEntity>> {
        return categoryRepository.getCategoriesForUserFlow(userId ?: "")
    }

    suspend fun createCategory(categoryName: String, color: Color): Result<Unit> {
        val currentUserId = userId ?: return Result.failure(IllegalStateException("User not logged in"))
        if (categoryName.isBlank()) return Result.failure(IllegalArgumentException("Category name cannot be blank"))

        val hexColor = String.format("#%06X", (0xFFFFFF and color.toArgb()))

        return categoryRepository.createCategory(
            userId = currentUserId,
            categoryName = categoryName,
            colorHex = hexColor
        )
    }
}
