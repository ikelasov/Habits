package com.example.habits.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.habits.data.localdatasource.habits.HabitDao
import com.example.habits.data.localdatasource.habits.HabitEntity

@Database(entities = [HabitEntity::class], version = 1, exportSchema = false)
abstract class HabitsRoomDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao

    companion object {

        @Volatile
        private var INSTANCE: HabitsRoomDatabase? = null

        fun getDatabase(context: Context): HabitsRoomDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HabitsRoomDatabase::class.java,
                    "habits_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}