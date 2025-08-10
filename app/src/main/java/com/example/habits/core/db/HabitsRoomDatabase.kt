package com.example.habits.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.habits.core.data.habitcategories.localdatasource.CategoryDao
import com.example.habits.core.data.habits.localdatasource.HabitDao
import com.example.habits.core.data.quotes.localdatasource.QuoteDao
import com.example.habits.core.db.typeconverters.DaysOfWeekTypeConverter
import com.example.habits.core.db.typeconverters.ReminderTimeTypeConverter
import com.example.habits.core.model.habitcategory.HabitCategoryEntity
import com.example.habits.core.model.habits.HabitEntity
import com.example.habits.core.model.quotes.QuoteEntity

@Database(
    entities = [HabitEntity::class, HabitCategoryEntity::class, QuoteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DaysOfWeekTypeConverter::class, ReminderTimeTypeConverter::class)
abstract class HabitsRoomDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitCategoryDao(): CategoryDao
    abstract fun quoteDao(): QuoteDao

    companion object {
        @Volatile
        private var INSTANCE: HabitsRoomDatabase? = null

        fun getDatabase(context: Context): HabitsRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        HabitsRoomDatabase::class.java,
                        "habits_database",
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}