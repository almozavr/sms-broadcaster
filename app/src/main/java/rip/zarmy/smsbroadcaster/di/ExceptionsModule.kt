package rip.zarmy.smsbroadcaster.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.dsl.module
import rip.zarmy.smsbroadcaster.BuildConfig
import rip.zarmy.smsbroadcaster.shared.exception.data.CrashlyticsExceptionHandler
import rip.zarmy.smsbroadcaster.shared.exception.data.LogExceptionHandler
import rip.zarmy.smsbroadcaster.shared.exception.data.MainExceptionHandler
import rip.zarmy.smsbroadcaster.shared.exception.domain.ExceptionHandler

val exceptionsModule = module {
    single { FirebaseCrashlytics.getInstance() }

    single {
        val exceptionHandlers = mutableListOf<ExceptionHandler>()
        if (BuildConfig.LOGGER_ENABLED) {
            exceptionHandlers.add(LogExceptionHandler())
        }
        exceptionHandlers.add(
            CrashlyticsExceptionHandler(get())
        )
        exceptionHandlers
    }
    single<ExceptionHandler> { MainExceptionHandler(get()) }
}
