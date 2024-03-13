package com.example.habits.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.habits.MainActivity
import com.example.habits.R
import com.example.habits.data.repository.HabitsRepository
import com.example.habits.worker.utils.HABIT_REMINDER_CHANNEL_ID
import com.example.habits.worker.utils.HABIT_REMINDER_CHANNEL_NAME
import com.example.habits.worker.utils.INPUT_DATA_HABIT_ID
import com.example.habits.worker.utils.findNextRemindersOffsetFromNow
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration

@HiltWorker
class HabitRemindersWorker
    @AssistedInject
    constructor(
        @Assisted private val appContext: Context,
        @Assisted private val params: WorkerParameters,
        private val habitsRepository: HabitsRepository,
    ) : CoroutineWorker(appContext, params) {
        override suspend fun doWork(): Result {
            return withContext(Dispatchers.IO) {
                val habitId = inputData.getInt(INPUT_DATA_HABIT_ID, -1)
                if (habitId == -1) Result.failure()

                val habit = habitsRepository.getHabit(habitId)

                makeReminderNotification(habit.name, appContext)

                val notificationOffset =
                    findNextRemindersOffsetFromNow(habit.daysToRepeat, habit.reminderTimes)

                val inputData = Data.Builder().putInt(INPUT_DATA_HABIT_ID, habitId).build()
                val request =
                    OneTimeWorkRequestBuilder<HabitRemindersWorker>()
                        .setInputData(inputData)
                        .setInitialDelay(Duration.ofSeconds(notificationOffset))
                        .build()

                WorkManager.getInstance(appContext).enqueue(request)
                Result.success()
            }
        }

        private fun makeReminderNotification(
            message: String,
            context: Context,
        ) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(
                    HABIT_REMINDER_CHANNEL_ID,
                    HABIT_REMINDER_CHANNEL_NAME,
                    importance,
                )

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)

            val pendingIntent: PendingIntent = createPendingIntent(context)

            val builder =
                NotificationCompat.Builder(context, HABIT_REMINDER_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(context.getString(R.string.time_for_progress))
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(LongArray(0))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

            NotificationManagerCompat.from(context).notify(1, builder.build())
        }

        private fun createPendingIntent(appContext: Context): PendingIntent {
            val intent =
                Intent(appContext, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }

            // Flag to detect unsafe launches of intents for Android 12 and higher
            // to improve platform security
            var flags = PendingIntent.FLAG_UPDATE_CURRENT
            flags = flags or PendingIntent.FLAG_IMMUTABLE

            return PendingIntent.getActivity(
                appContext,
                1,
                intent,
                flags,
            )
        }
    }
