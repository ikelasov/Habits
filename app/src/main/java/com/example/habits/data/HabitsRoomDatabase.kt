package com.example.habits.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.habits.data.localdatasource.habits.HabitDao
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.data.localdatasource.typeconverters.DaysOfWeekTypeConverter
import com.example.habits.data.localdatasource.typeconverters.ReminderTimeTypeConverter

@Database(entities = [HabitEntity::class], version = 1, exportSchema = false)
@TypeConverters(DaysOfWeekTypeConverter::class, ReminderTimeTypeConverter::class)
abstract class HabitsRoomDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        @Volatile
        private var INSTANCE: HabitsRoomDatabase? = null

        fun getDatabase(context: Context): HabitsRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        HabitsRoomDatabase::class.java,
                        "habits_database",
                    ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
