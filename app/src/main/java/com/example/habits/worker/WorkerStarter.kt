package com.example.habits.worker

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.worker.utils.INPUT_DATA_HABIT_ID
import com.example.habits.worker.utils.WORK_MANAGER_REMINDER_TAG
import com.example.habits.worker.utils.findNextRemindersOffsetFromNow
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerStarter
    @Inject
    constructor(context: Context) {
        private val workManager = WorkManager.getInstance(context)

        fun startWork(habitToSetReminder: HabitEntity) {
            val inputData = Data.Builder()
            inputData.putInt(INPUT_DATA_HABIT_ID, habitToSetReminder.id)

            val initialDelayInSeconds =
                findNextRemindersOffsetFromNow(
                    habitToSetReminder.daysToRepeat,
                    habitToSetReminder.reminderTimes,
                )

            buildWorkRequest(
                initialDelayInSeconds,
                inputData,
                habitToSetReminder.id,
            ).enqueue()
        }

        private fun OneTimeWorkRequest.enqueue() {
            workManager.enqueue(this)
        }

        private fun buildWorkRequest(
            initialDelayInSeconds: Long,
            inputData: Data.Builder,
            habitId: Int,
        ): OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<HabitRemindersWorker>()
                .setInitialDelay(Duration.ofSeconds(initialDelayInSeconds))
                .setInputData(inputData.build())
                .addTag(WORK_MANAGER_REMINDER_TAG + habitId)
                .build()
    }
