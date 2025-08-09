package com.example.habits.data.habitcategories

import com.example.habits.data.habitcategories.repository.CategoryRepository
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CategorySyncManager @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    fun startListeningForCategoryChanges(userId: String) {
        categoryRepository.startListeningForCategoryChanges(userId)
    }

    fun stopListeningForCategoryChanges() {
        categoryRepository.stopListeningForCategoryChanges()
    }
}
