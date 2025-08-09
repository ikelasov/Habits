package com.example.habits.data.localdatasource.habitscategory

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_categories_table")
data class HabitCategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val color: String,
    val isDefault: Boolean,
    val userId: String,
    val createdAt: Long
)