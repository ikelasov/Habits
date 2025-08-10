package com.example.habits.core.data.habitcategories.localdatasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.habits.core.model.habitcategory.HabitCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Transaction
    suspend fun replaceAllForUser(userId: String, categories: List<HabitCategoryEntity>) {
        clearUserCategories(userId)
        insertAll(categories)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: HabitCategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<HabitCategoryEntity>)

    @Update
    suspend fun update(category: HabitCategoryEntity)

    @Delete
    suspend fun delete(category: HabitCategoryEntity)

    @Query("DELETE FROM user_categories_table WHERE id = :categoryId AND userId = :userId")
    suspend fun deleteByIdAndUserId(categoryId: String, userId: String)

    @Query("SELECT * FROM user_categories_table WHERE id = :categoryId AND userId = :userId")
    suspend fun getCategoryByIdAndUserId(categoryId: String, userId: String): HabitCategoryEntity?

    @Query("SELECT * FROM user_categories_table WHERE userId = :userId ORDER BY name ASC")
    fun getCategoriesForUserFlow(userId: String): Flow<List<HabitCategoryEntity>>

    @Query("DELETE FROM user_categories_table WHERE userId = :userId")
    suspend fun clearUserCategories(userId: String)
}