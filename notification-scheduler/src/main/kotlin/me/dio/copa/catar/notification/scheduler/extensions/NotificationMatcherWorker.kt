package me.dio.copa.catar.notification.scheduler.extensions

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val NOTIFICATION_TITLE_KEY = "NOTIFICATION_TITLE_KEY"
private const val NOTIFICATION_CONTENT_KEY = "NOTIFICATION_CONTENT_KEY"

class NotificationMatcherWorker(
    private val context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val title = inputData.getString(NOTIFICATION_TITLE_KEY)
            ?: throw IllegalArgumentException("title is required")
        val content = inputData.getString(NOTIFICATION_CONTENT_KEY)
            ?: throw IllegalArgumentException("content is required")

        context.showNotification(title, content)

        return Result.success()
    }

    companion object {
        fun start(context: Context, match: MatchDomain, team1: TeamDomain, team2: TeamDomain) {
            val matchDate = LocalDateTime.parse(match.date, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            val initialDelay = Duration.between(LocalDateTime.now(), matchDate).minusMinutes(5)

            if (initialDelay.isNegative) return

            val inputData = workDataOf(
                NOTIFICATION_TITLE_KEY to "Se prepare que o jogo vai começar",
                NOTIFICATION_CONTENT_KEY to "Hoje tem ${team1.name} vs ${team2.name}",
            )

            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    match.id.toString(),
                    ExistingWorkPolicy.KEEP,
                    createRequest(initialDelay, inputData)
                )
        }

        fun cancel(context: Context, match: MatchDomain) {
            WorkManager.getInstance(context)
                .cancelUniqueWork(match.id.toString())
        }

        private fun createRequest(initialDelay: Duration, inputData: Data): OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<NotificationMatcherWorker>()
                .setInitialDelay(initialDelay)
                .setInputData(inputData)
                .build()
    }
}
