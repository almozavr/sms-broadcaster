package ua.sprotyv.smsbroadcaster.feature.sender.data

import androidx.lifecycle.asFlow
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.takeWhile
import ua.sprotyv.smsbroadcaster.feature.sender.domain.SendResult
import ua.sprotyv.smsbroadcaster.feature.sender.domain.SmsRepository

class WorkManagerSmsRepository(private val workManager: WorkManager) : SmsRepository {

    companion object {
        private const val WORK_NAME = "immediate_sms_work"
    }

    override fun send(body: String, phones: List<String>): Flow<SendResult> {
        val request = OneTimeWorkRequestBuilder<SendSmsWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(
                workDataOf(
                    SendSmsWorker.ARG_SMS to body,
                    SendSmsWorker.ARG_PHONES to phones.toTypedArray()
                )
            )
            .build()
        return workManager.getWorkInfosForUniqueWorkLiveData(WORK_NAME).asFlow()
            .takeWhile {
                val work = it.first()
                if (work.state == WorkInfo.State.FAILED) throw IllegalStateException("Work failed")
                work.state !in listOf(WorkInfo.State.SUCCEEDED, WorkInfo.State.CANCELLED)
            }
            .map {
                val work = it.first()
                val progress =
                    if (work.state != WorkInfo.State.SUCCEEDED) work.progress.getInt(SendSmsWorker.ARG_PROGRESS, 0)
                    else work.outputData.getInt(SendSmsWorker.ARG_PROGRESS, 0)
                SendResult(sent = progress)
            }
            .onStart {
                workManager.enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.REPLACE, request)
            }
    }

    override fun cancel() {
        workManager.cancelUniqueWork(WORK_NAME)
    }
}

class GracefulCompletionException : RuntimeException("Completed gracefully")
