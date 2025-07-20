package com.example.habits.data.localdatasource.habitscategory

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_category_table")
data class HabitCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)