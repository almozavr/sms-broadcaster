package ua.sprotyv.smsbroadcaster.feature.sender.data

import android.app.Notification
import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import ua.sprotyv.smsbroadcaster.R

class SendSmsWorker(private val context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {

    companion object {
        const val ARG_SMS = "sms"
        const val ARG_PHONES = "phones"
        const val ARG_PROGRESS = "progress"
        private const val NOTIFICATION_ID = 123
        private const val NOTIFICATION_CHANNEL = "default"
    }

    private val smsManager: SmsManager by lazy { context.getSystemService(SmsManager::class.java) }

    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo())
        val sms = inputData.getString(ARG_SMS) ?: return Result.failure()
        val phones = inputData.getStringArray(ARG_PHONES) ?: return Result.failure()
        phones.forEachIndexed { i, phone ->
            smsManager.sendTextMessage(phone, null, sms, null, null)
            setProgress(workDataOf(ARG_PROGRESS to i))
        }
        return Result.success(workDataOf(ARG_PROGRESS to phones.size))
    }

    private fun createForegroundInfo() =
        ForegroundInfo(NOTIFICATION_ID, createNotification())

    private fun createNotification(): Notification {
        val id = NOTIFICATION_CHANNEL
        val title = applicationContext.getString(R.string.send_notification_title)
        val text = applicationContext.getString(R.string.send_notification_text)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel()

        return NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .build()
    }

    private fun createChannel() {
        val channel = NotificationChannelCompat.Builder(NOTIFICATION_CHANNEL, NotificationManagerCompat.IMPORTANCE_HIGH)
            .setName(context.getString(R.string.send_notification_channel))
            .build()
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }
}
