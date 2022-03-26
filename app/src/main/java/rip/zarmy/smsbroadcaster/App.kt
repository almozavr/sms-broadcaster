package rip.zarmy.smsbroadcaster

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import rip.zarmy.smsbroadcaster.di.exceptionsModule
import rip.zarmy.smsbroadcaster.di.fetcherModule
import rip.zarmy.smsbroadcaster.di.mainModule
import rip.zarmy.smsbroadcaster.di.netModule
import rip.zarmy.smsbroadcaster.di.permissionsModule
import rip.zarmy.smsbroadcaster.di.senderModule
import timber.log.Timber

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
            androidLogger(if (BuildConfig.LOGGER_ENABLED) Level.ERROR else Level.NONE)
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
