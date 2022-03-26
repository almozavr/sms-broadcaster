package rip.zarmy.smsbroadcaster.feature.sender.data

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import rip.zarmy.smsbroadcaster.MainActivity
import rip.zarmy.smsbroadcaster.R

class SmsNotificationFactory(private val context: Context) {

    companion object {
        const val NOTIFICATION_ID = 123
        private const val NOTIFICATION_CHANNEL = "default"
    }

    fun createNotification(): Notification {
        val id = NOTIFICATION_CHANNEL
        val title = context.getString(R.string.send_notification_title)
        val text = context.getString(R.string.send_notification_text)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent: PendingIntent? = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, id)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(text)
            .setTicker(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .build()
    }

    private fun createChannel() {
        val channel = NotificationChannelCompat.Builder(
            NOTIFICATION_CHANNEL,
            NotificationManagerCompat.IMPORTANCE_HIGH
        )
            .setName(context.getString(R.string.send_notification_channel))
            .build()
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }
}
