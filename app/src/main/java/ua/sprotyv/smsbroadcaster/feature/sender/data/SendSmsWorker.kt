package ua.sprotyv.smsbroadcaster.feature.sender.data

import android.app.PendingIntent
import android.content.Context
import android.telephony.SmsManager
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class SendSmsWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {

    companion object {
        const val ARG_SMS = "sms"
        const val ARG_PHONES = "phones"
        const val ARG_PROGRESS = "progress"
    }

    private val smsManager: SmsManager by lazy { context.getSystemService(SmsManager::class.java) }
    private val notificationFactory by lazy { SmsNotificationFactory(applicationContext) }

    override suspend fun doWork(): Result {
        ForegroundInfo(SmsNotificationFactory.NOTIFICATION_ID, notificationFactory.createNotification()).also {
            setForeground(it)
        }
        val smsBody = inputData.getString(ARG_SMS) ?: return Result.failure()
        val phones = inputData.getStringArray(ARG_PHONES) ?: return Result.failure()
        phones.forEachIndexed { i, phone ->
            if (isStopped) return@forEachIndexed
            val sentIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                SendSmsResultReceiver.create(phone),
                PendingIntent.FLAG_IMMUTABLE
            )
            smsManager.sendTextMessage(phone, null, smsBody, sentIntent, null)
            setProgress(
                workDataOf(
                    ARG_SMS to smsBody,
                    ARG_PROGRESS to i + 1,
                    ARG_PHONES to phones
                )
            )
        }
        return Result.success(
            workDataOf(
                ARG_SMS to smsBody,
                ARG_PROGRESS to phones.size,
                ARG_PHONES to phones
            )
        )
    }
}
