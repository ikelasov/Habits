package com.example.habits.core.worker

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.habits.core.model.habits.HabitEntity
import com.example.habits.core.worker.utils.INPUT_DATA_HABIT_ID
import com.example.habits.core.worker.utils.WORK_MANAGER_REMINDER_TAG
import com.example.habits.core.worker.utils.findNextRemindersOffsetFromNow
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerStarter @Inject constructor(context: Context) {
    private val workManager = WorkManager.getInstance(context)

    fun startWork(habitToSetReminder: HabitEntity) {
        val inputData = Data.Builder()
        inputData.putString(INPUT_DATA_HABIT_ID, habitToSetReminder.id)

        val initialDelayInSeconds =
            findNextRemindersOffsetFromNow(
                habitToSetReminder.daysToRepeat,
                habitToSetReminder.reminderTimes,
            )

        val workRequest = buildWorkRequest(
            initialDelayInSeconds,
            inputData,
            habitToSetReminder.id,
        )

        workManager.enqueueUniqueWork(
            "habit_reminder_${habitToSetReminder.id}",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun buildWorkRequest(
        initialDelayInSeconds: Long,
        inputData: Data.Builder,
        habitId: String,
    ): OneTimeWorkRequest =
        OneTimeWorkRequestBuilder<HabitRemindersWorker>()
            .setInitialDelay(Duration.ofSeconds(initialDelayInSeconds))
            .setInputData(inputData.build())
            .addTag(WORK_MANAGER_REMINDER_TAG + habitId)
            .build()
}
