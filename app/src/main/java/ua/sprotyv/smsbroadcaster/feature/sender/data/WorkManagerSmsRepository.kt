package ua.sprotyv.smsbroadcaster.feature.sender.data

import androidx.lifecycle.asFlow
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.await
import androidx.work.workDataOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import ua.sprotyv.smsbroadcaster.feature.sender.domain.SmsRepository
import ua.sprotyv.smsbroadcaster.feature.sender.domain.entity.SmsSendStatus
import ua.sprotyv.smsbroadcaster.shared.entity.Status

class WorkManagerSmsRepository(private val workManager: WorkManager) : SmsRepository {

    companion object {
        private const val WORK_NAME = "immediate_sms_work"
    }

    override fun observe(): Flow<SmsSendStatus> =
        workManager.getWorkInfosForUniqueWorkLiveData(WORK_NAME).asFlow()
            .filter {
                val work = it.first()
                work.state != WorkInfo.State.ENQUEUED && work.state != WorkInfo.State.BLOCKED &&
                    (work.progress.keyValueMap.isNotEmpty() || work.outputData.keyValueMap.isNotEmpty())
            }
            .map {
                if (it.isEmpty()) return@map SmsSendStatus("", emptyList(), 0, Status.IDLE)
                val work = it.first()
                when {
                    work.state == WorkInfo.State.CANCELLED -> work.progress.asSendResult(Status.COMPLETE)
                    work.state != WorkInfo.State.SUCCEEDED -> work.progress.asSendResult(Status.PROGRESS)
                    else -> work.outputData.asSendResult(Status.COMPLETE)
                }
            }

    override suspend fun send(body: String, phones: List<String>) {
        val request = OneTimeWorkRequestBuilder<SendSmsWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(
                workDataOf(
                    SendSmsWorker.ARG_SMS to body,
                    SendSmsWorker.ARG_PHONES to phones.toTypedArray()
                )
            )
            .build()
        workManager.enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.REPLACE, request).await()
    }

    override suspend fun cancel() {
        workManager.cancelUniqueWork(WORK_NAME)
    }
}

private fun Data.asSendResult(status: Status) = SmsSendStatus(
    sent = getInt(SendSmsWorker.ARG_PROGRESS, 0),
    phones = getStringArray(SendSmsWorker.ARG_PHONES)?.toList() ?: emptyList(),
    body = getString(SendSmsWorker.ARG_SMS) ?: "",
    status = status,
)
