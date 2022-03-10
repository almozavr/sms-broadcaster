package ua.sprotyv.smsbroadcaster.shared.exception.data

import com.google.firebase.crashlytics.FirebaseCrashlytics
import ua.sprotyv.smsbroadcaster.shared.exception.domain.ExceptionHandler

class CrashlyticsExceptionHandler(
    private val firebaseCrashlytics: FirebaseCrashlytics
) : ExceptionHandler {

    override fun handle(error: Throwable) {
        firebaseCrashlytics.recordException(error)
    }
}
