package com.example.habits.data.habitscategory.localdatasource

import com.example.habits.data.model.habitcategory.HabitCategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(
    private val habitCategoryDao: HabitCategoryDao
) {

    fun getCategoriesForUserFlow(userId: String): Flow<List<HabitCategoryEntity>> {
        return habitCategoryDao.getCategoriesForUserFlow(userId)
    }

    suspend fun insert(habitCategoryEntity: HabitCategoryEntity) {
        habitCategoryDao.insert(habitCategoryEntity)
    }

    suspend fun insertAll(categories: List<HabitCategoryEntity>) {
        habitCategoryDao.insertAll(categories)
    }
}