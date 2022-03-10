package ua.sprotyv.smsbroadcaster.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.dsl.module
import ua.sprotyv.smsbroadcaster.BuildConfig
import ua.sprotyv.smsbroadcaster.shared.exception.data.CrashlyticsExceptionHandler
import ua.sprotyv.smsbroadcaster.shared.exception.data.LogExceptionHandler
import ua.sprotyv.smsbroadcaster.shared.exception.data.MainExceptionHandler
import ua.sprotyv.smsbroadcaster.shared.exception.domain.ExceptionHandler

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
