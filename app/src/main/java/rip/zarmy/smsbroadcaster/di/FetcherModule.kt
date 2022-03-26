package rip.zarmy.smsbroadcaster.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import rip.zarmy.smsbroadcaster.feature.fetcher.data.BroadcastApiService
import rip.zarmy.smsbroadcaster.feature.fetcher.data.BroadcastFetcherApiSource
import rip.zarmy.smsbroadcaster.feature.fetcher.data.CredentialsDataStoreSource
import rip.zarmy.smsbroadcaster.feature.fetcher.domain.BroadcastFetcherSource
import rip.zarmy.smsbroadcaster.feature.fetcher.domain.CredentialsSource
import rip.zarmy.smsbroadcaster.feature.fetcher.domain.FetcherRepository

val fetcherModule = module {
    factory { get<Retrofit>().create(BroadcastApiService::class.java) }
    factory<BroadcastFetcherSource> { BroadcastFetcherApiSource(get()) }
    factory<CredentialsSource> { CredentialsDataStoreSource(androidContext()) }
    factory { FetcherRepository(get(), get()) }
}
