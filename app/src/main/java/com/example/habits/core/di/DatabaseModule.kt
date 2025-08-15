package com.example.habits.core.di

import android.content.Context
import com.example.habits.core.data.habitcategories.localdatasource.CategoryDao
import com.example.habits.core.data.habits.localdatasource.HabitCompletionDao
import com.example.habits.core.data.habits.localdatasource.HabitDao
import com.example.habits.core.data.quotes.localdatasource.QuoteDao
import com.example.habits.core.db.HabitsRoomDatabase
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
    fun provideHabitsDao(habitsDatabase: HabitsRoomDatabase): HabitDao =
        habitsDatabase.habitDao()

    @Provides
    fun provideHabitCompletionDao(habitsDatabase: HabitsRoomDatabase): HabitCompletionDao =
        habitsDatabase.habitCompletionDao()

    @Provides
    fun provideHabitsCategoriesDao(habitsDatabase: HabitsRoomDatabase): CategoryDao =
        habitsDatabase.habitCategoryDao()

    @Provides
    fun provideQuoteDao(habitsDatabase: HabitsRoomDatabase): QuoteDao =
        habitsDatabase.quoteDao()

    @Provides
    @Singleton
    fun provideHabitsDatabase(@ApplicationContext applicationContext: Context): HabitsRoomDatabase =
        HabitsRoomDatabase.getDatabase(applicationContext)
}
