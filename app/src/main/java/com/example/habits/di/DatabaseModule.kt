package com.example.habits.di

import android.content.Context
import com.example.habits.data.HabitsRoomDatabase
import com.example.habits.data.habits.localdatasource.HabitDao
import com.example.habits.data.habitscategory.localdatasource.CategoryDao
import com.example.habits.data.quotes.localdatasource.QuoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideHabitsDao(habitsDatabase: HabitsRoomDatabase): HabitDao {
        return habitsDatabase.habitDao()
    }

    @Provides
    fun provideHabitsCategoriesDao(habitsDatabase: HabitsRoomDatabase): CategoryDao {
        return habitsDatabase.habitCategoryDao()
    }

    @Provides
    fun provideQuoteDao(habitsDatabase: HabitsRoomDatabase): QuoteDao {
        return habitsDatabase.quoteDao()
    }

    @Provides
    @Singleton
    fun provideHabitsDatabase(
        @ApplicationContext applicationContext: Context,
    ): HabitsRoomDatabase {
        return HabitsRoomDatabase.getDatabase(applicationContext)
    }
}
