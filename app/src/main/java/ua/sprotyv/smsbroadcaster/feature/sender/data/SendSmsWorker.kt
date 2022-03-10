package ua.sprotyv.smsbroadcaster.feature.sender.data

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import ua.sprotyv.smsbroadcaster.MainActivity
import ua.sprotyv.smsbroadcaster.R

class SendSmsWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {

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
            if (isStopped) return@forEachIndexed
            smsManager.sendTextMessage(phone, null, sms, null, null)
            setProgress(
                workDataOf(
                    ARG_SMS to sms,
                    ARG_PROGRESS to i,
                    ARG_PHONES to phones
                )
            )
        }
        return Result.success(
            workDataOf(
                ARG_SMS to sms,
                ARG_PROGRESS to phones.size,
                ARG_PHONES to phones
            )
        )
    }

    private fun createForegroundInfo() =
        ForegroundInfo(NOTIFICATION_ID, createNotification())

    private fun createNotification(): Notification {
        val id = NOTIFICATION_CHANNEL
        val title = applicationContext.getString(R.string.send_notification_title)
        val text = applicationContext.getString(R.string.send_notification_text)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel()

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent: PendingIntent? = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(applicationContext, id)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(text)
            .setTicker(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .build()
    }

    private fun createChannel() {
        val channel = NotificationChannelCompat.Builder(NOTIFICATION_CHANNEL, NotificationManagerCompat.IMPORTANCE_HIGH)
            .setName(applicationContext.getString(R.string.send_notification_channel))
            .build()
        NotificationManagerCompat.from(applicationContext).createNotificationChannel(channel)
    }
}
