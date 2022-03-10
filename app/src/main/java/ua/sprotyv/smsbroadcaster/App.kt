package ua.sprotyv.smsbroadcaster

import android.app.Application
import androidx.viewbinding.BuildConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import ua.sprotyv.smsbroadcaster.di.exceptionsModule
import ua.sprotyv.smsbroadcaster.di.fetcherModule
import ua.sprotyv.smsbroadcaster.di.mainModule
import ua.sprotyv.smsbroadcaster.di.netModule

@ExperimentalCoroutinesApi
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDependencyInjection()
    }

    private fun setupDependencyInjection(): KoinApplication =
        startKoin {
            androidContext(this@App)
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            modules(
                // region main
                mainModule,
                // endregion
                // region io
                netModule,
                fetcherModule,
                // endregion
                // region shared
                exceptionsModule,
                // endregion
            )
        }
}
