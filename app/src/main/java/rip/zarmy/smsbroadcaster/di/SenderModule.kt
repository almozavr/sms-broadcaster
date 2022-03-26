package rip.zarmy.smsbroadcaster.di

import androidx.work.WorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import rip.zarmy.smsbroadcaster.feature.sender.data.SendSmsResultReceiver
import rip.zarmy.smsbroadcaster.feature.sender.data.WorkManagerSmsRepository
import rip.zarmy.smsbroadcaster.feature.sender.domain.SmsRepository

val senderModule = module {
    single { WorkManager.getInstance(androidContext()) }
    factory<SmsRepository> { WorkManagerSmsRepository(get()) }
    single(createdAtStart = true) { SendSmsResultReceiver(androidContext(), get()) }
}
