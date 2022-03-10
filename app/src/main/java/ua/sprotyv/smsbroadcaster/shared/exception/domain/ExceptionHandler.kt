package ua.sprotyv.smsbroadcaster.shared.exception.domain

import kotlinx.coroutines.CoroutineExceptionHandler

fun interface ExceptionHandler {
    fun handle(error: Throwable)
}

fun ExceptionHandler.asCoroutineExceptionHandler() =
    CoroutineExceptionHandler { _, throwable -> this.handle(throwable) }
