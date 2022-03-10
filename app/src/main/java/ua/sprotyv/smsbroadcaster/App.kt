package ua.sprotyv.smsbroadcaster

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import ua.sprotyv.smsbroadcaster.di.exceptionsModule
import ua.sprotyv.smsbroadcaster.di.fetcherModule
import ua.sprotyv.smsbroadcaster.di.mainModule
import ua.sprotyv.smsbroadcaster.di.netModule
import ua.sprotyv.smsbroadcaster.di.permissionsModule
import ua.sprotyv.smsbroadcaster.di.senderModule

@ExperimentalCoroutinesApi
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupLogger()
        setupDependencyInjection()
    }

    private fun setupLogger() {
        if (BuildConfig.LOGGER_ENABLED) Timber.plant(Timber.DebugTree())
    }

    private fun setupDependencyInjection(): KoinApplication =
        startKoin {
            androidContext(this@App)
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.INFO)
            modules(
                // region main
                mainModule,
                // endregion
                // region io
                netModule,
                fetcherModule,
                senderModule,
                // endregion
                // region shared
                exceptionsModule,
                permissionsModule,
                // endregion
            )
        }
}
