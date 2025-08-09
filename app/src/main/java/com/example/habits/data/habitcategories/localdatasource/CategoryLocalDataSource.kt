package com.example.habits.data.habitscategory.localdatasource

import com.example.habits.data.model.habitcategory.HabitCategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(
    private val categoryDao: CategoryDao
) {

    fun getCategoriesForUserFlow(userId: String): Flow<List<HabitCategoryEntity>> {
        return categoryDao.getCategoriesForUserFlow(userId)
    }

    suspend fun insert(habitCategoryEntity: HabitCategoryEntity) {
        categoryDao.insert(habitCategoryEntity)
    }

    suspend fun insertAll(categories: List<HabitCategoryEntity>) {
        categoryDao.insertAll(categories)
    }

    suspend fun replaceAllCategoriesForUser(userId: String, categories: List<HabitCategoryEntity>) {
        categoryDao.replaceAllForUser(userId, categories)
    }
}