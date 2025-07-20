package com.example.habits.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.habits.data.localdatasource.habits.HabitDao
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.data.localdatasource.habitscategory.HabitCategoryDao
import com.example.habits.data.localdatasource.habitscategory.HabitCategoryEntity
import com.example.habits.data.localdatasource.typeconverters.DaysOfWeekTypeConverter
import com.example.habits.data.localdatasource.typeconverters.ReminderTimeTypeConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [HabitEntity::class, HabitCategoryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DaysOfWeekTypeConverter::class, ReminderTimeTypeConverter::class)
abstract class HabitsRoomDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitCategoryDao(): HabitCategoryDao

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
                    .addCallback(InitialCategoriesCallback())
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class InitialCategoriesCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateCategories(database.habitCategoryDao())
                }
            }
        }

        suspend fun populateCategories(habitCategoryDao: HabitCategoryDao) {
            // 3. Define the list of default categories.
            val defaultCategories = listOf(
                HabitCategoryEntity(name = "Fitness"),
                HabitCategoryEntity(name = "Health"),
                HabitCategoryEntity(name = "Work"),
                HabitCategoryEntity(name = "Hobbies"),
                HabitCategoryEntity(name = "Personal Growth")
            )

            // 4. Insert them into the database.
            defaultCategories.forEach { category ->
                habitCategoryDao.insertCategory(category)
            }
        }
    }
}
