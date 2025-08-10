package com.example.habits.core.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.habits.core.data.habits.repository.HabitRepository
import javax.inject.Inject

class HabitRemindersWorkerFactory @Inject constructor(private val habitRepository: HabitRepository) :
    WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker = HabitRemindersWorker(appContext, workerParameters, habitRepository)
}
