package com.example.habits.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.habits.data.habits.localdatasource.HabitDao
import com.example.habits.data.habits.localdatasource.HabitEntity
import com.example.habits.data.habitscategory.localdatasource.HabitCategoryDao
import com.example.habits.data.habitscategory.localdatasource.HabitCategoryEntity
import com.example.habits.data.quotes.localdatasource.QuoteDao
import com.example.habits.data.quotes.localdatasource.QuoteEntity
import com.example.habits.data.habits.localdatasource.typeconverters.DaysOfWeekTypeConverter
import com.example.habits.data.habits.localdatasource.typeconverters.ReminderTimeTypeConverter

@Database(
    entities = [HabitEntity::class, HabitCategoryEntity::class, QuoteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DaysOfWeekTypeConverter::class, ReminderTimeTypeConverter::class)
abstract class HabitsRoomDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitCategoryDao(): HabitCategoryDao
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
