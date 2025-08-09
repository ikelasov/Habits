package com.example.habits.data.model.habitcategory

import androidx.core.graphics.toColorInt
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class FirestoreHabitCategory(
    val id: String = "",
    val name: String = "",
    val color: String = "",
    val isDefault: Boolean = false,
    val userId: String = "",
    @ServerTimestamp
    val createdAt: Date? = null
)

fun FirestoreHabitCategory.toHabitCategoryEntity(): HabitCategoryEntity =
    HabitCategoryEntity(
        id = id,
        name = name,
        color = color.toColorInt(),
        isDefault = isDefault,
        userId = userId,
        createdAt = createdAt?.time ?: System.currentTimeMillis()
    )