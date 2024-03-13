package com.example

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.example.habits.worker.HabitRemindersWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HabitsApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var customWorkerFactory: HabitRemindersWorkerFactory

    override val workManagerConfiguration: Configuration
        get() =
            Configuration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .setWorkerFactory(customWorkerFactory)
                .build()
}
