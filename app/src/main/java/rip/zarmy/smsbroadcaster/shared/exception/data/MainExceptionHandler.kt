package rip.zarmy.smsbroadcaster.shared.exception.data

import rip.zarmy.smsbroadcaster.shared.exception.domain.ExceptionHandler

class MainExceptionHandler(
    private val exceptionHandlers: List<ExceptionHandler>
) : ExceptionHandler {

    override fun handle(error: Throwable) {
        exceptionHandlers.forEach { it.handle(error) }
    }
}
