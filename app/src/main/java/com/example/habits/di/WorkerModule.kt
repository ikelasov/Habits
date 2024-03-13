package com.example.habits.di

import android.content.Context
import com.example.habits.worker.WorkerStarter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WorkerModule {
    @Singleton
    @Provides
    fun provideWorkerStarter(
        @ApplicationContext context: Context,
    ) = WorkerStarter(context)
}
