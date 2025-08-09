package com.example.habits.data.habitscategory.localdatasource

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_categories_table")
data class HabitCategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val color: Int,
    val isDefault: Boolean = false,
    val userId: String,
    val createdAt: Long
)