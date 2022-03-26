package rip.zarmy.smsbroadcaster.shared.exception.data

import rip.zarmy.smsbroadcaster.shared.exception.domain.ExceptionHandler
import timber.log.Timber

class LogExceptionHandler : ExceptionHandler {

    override fun handle(error: Throwable) {
        Timber.w(error)
    }
}
