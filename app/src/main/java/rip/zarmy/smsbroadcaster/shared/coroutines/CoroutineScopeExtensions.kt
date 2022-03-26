package rip.zarmy.smsbroadcaster.shared.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import rip.zarmy.smsbroadcaster.shared.kotlin.runSuspendCatching

@Suppress("RedundantSuspendModifier", "SuspendFunctionOnCoroutineScope")
suspend inline fun <R> CoroutineScope.executeWithHandler(block: () -> R): Result<R> {
    val currentExecContext = coroutineScope { coroutineContext }
    val exceptionHandler = currentExecContext[CoroutineExceptionHandler]
    return runSuspendCatching(block)
        .onFailure {
            exceptionHandler?.handleException(currentExecContext, it)
        }
}
