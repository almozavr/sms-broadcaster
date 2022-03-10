package ua.sprotyv.smsbroadcaster.shared.exception.data

import timber.log.Timber
import ua.sprotyv.smsbroadcaster.shared.exception.domain.ExceptionHandler

class LogExceptionHandler : ExceptionHandler {

    override fun handle(error: Throwable) {
        Timber.w(error)
    }
}
