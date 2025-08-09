package com.example.habits.data.habitscategory

import com.example.habits.data.habitscategory.repository.CategoryRepository
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CategoriesSyncManager @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    fun startListeningForCategoryChanges(userId: String) {
        categoryRepository.startListeningForCategoryChanges(userId)
    }

    fun stopListeningForCategoryChanges() {
        categoryRepository.stopListeningForCategoryChanges()
    }
}
