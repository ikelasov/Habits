package com.example.habits.data

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.habits.data.localdatasource.habits.HabitDao
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.data.localdatasource.habitscategory.HabitCategoryDao
import com.example.habits.data.localdatasource.habitscategory.HabitCategoryEntity
import com.example.habits.data.localdatasource.quotes.QuoteDao
import com.example.habits.data.localdatasource.quotes.QuoteEntity
import com.example.habits.data.localdatasource.typeconverters.DaysOfWeekTypeConverter
import com.example.habits.data.localdatasource.typeconverters.ReminderTimeTypeConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateCategories(database.habitCategoryDao())
                    populateQuotes(database.quoteDao())
                }
            }
        }

        suspend fun populateCategories(habitCategoryDao: HabitCategoryDao) {
            val defaultCategories = listOf(
                HabitCategoryEntity(name = "Fitness", color = Color.Red.toArgb()),
                HabitCategoryEntity(name = "Health", color = Color.Blue.toArgb()),
                HabitCategoryEntity(name = "Work", color = Color.Yellow.toArgb()),
                HabitCategoryEntity(name = "Hobbies", color = Color.Cyan.toArgb()),
                HabitCategoryEntity(name = "Personal Growth", color = Color.Green.toArgb())
            )

            defaultCategories.forEach { category ->
                habitCategoryDao.insertCategory(category)
            }
        }

        suspend fun populateQuotes(quoteDao: QuoteDao) {
            val defaultQuotes = listOf(
                QuoteEntity(
                    text = "The secret of getting ahead is getting started.",
                    author = "Mark Twain"
                ),
                QuoteEntity(
                    text = "We are what we repeatedly do. Excellence, then, is not an act, but a habit.",
                    author = "Aristotle"
                ),
                QuoteEntity(
                    text = "The successful warrior is the average man, with laser-like focus.",
                    author = "Bruce Lee"
                ),
                QuoteEntity(
                    text = "Motivation is what gets you started. Habit is what keeps you going.",
                    author = "Jim Rohn"
                ),
                QuoteEntity(
                    text = "Your net worth to the world is usually determined by what remains after your bad habits are subtracted from your good ones.",
                    author = "Benjamin Franklin"
                ),
                QuoteEntity(
                    text = "The chains of habit are too weak to be felt until they are too strong to be broken.",
                    author = "Samuel Johnson"
                ),
                QuoteEntity(
                    text = "An object in motion stays in motion. Just get started.",
                    author = "James Clear"
                ),
                QuoteEntity(
                    text = "Success is the sum of small efforts, repeated day in and day out.",
                    author = "Robert Collier"
                ),
                QuoteEntity(
                    text = "First forget inspiration. Habit is more dependable. Habit will sustain you whether you're inspired or not.",
                    author = "Octavia Butler"
                ),
                QuoteEntity(
                    text = "A nail is driven out by another nail. Habit is overcome by habit.",
                    author = "Erasmus"
                )
            )

            defaultQuotes.forEach { quote ->
                quoteDao.insertQuote(quote)
            }
        }
    }
}
