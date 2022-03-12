package ua.sprotyv.smsbroadcaster.feature.sender.data

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class SendSmsResultReceiver(
    context: Context,
    private val firebaseCrashlytics: FirebaseCrashlytics
) : BroadcastReceiver() {

    init {
        context.registerReceiver(this, IntentFilter(INTENT_ACTION))
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val phone = intent?.extras?.getString(ARG_PHONE)
        if (resultCode == Activity.RESULT_OK) {
            Timber.i(message = "Sent to $phone: – ok")
        } else {
            val exception = SmsNotSentException(resultCode)
            Timber.w(message = "Sent to $phone: – fail", t = exception)
            firebaseCrashlytics.recordException(exception)
        }
    }

    companion object {
        private const val INTENT_ACTION = "SEND_SMS_RESULT_RECEIVER"
        private const val ARG_PHONE = "phone"

        fun create(phone: String) = Intent(INTENT_ACTION).apply {
            putExtra(ARG_PHONE, phone)
        }
    }
}

private class SmsNotSentException(failCode: Int) : Exception(
    "Fail code: $failCode. See https://developer.android.com/reference/android/telephony/SmsManager"
)
