package com.example.habits.data.localdatasource.habitscategory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitCategoryDao {

    @Query("SELECT * from habit_category_table ORDER BY name ASC")
    fun getCategoriesFlow(): Flow<List<HabitCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: HabitCategoryEntity)
}