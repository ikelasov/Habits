package com.example.habits.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.habits.data.repository.HabitsRepository
import javax.inject.Inject

class HabitRemindersWorkerFactory
    @Inject
    constructor(private val habitsRepository: HabitsRepository) :
    WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters,
        ): ListenableWorker = HabitRemindersWorker(appContext, workerParameters, habitsRepository)
    }
