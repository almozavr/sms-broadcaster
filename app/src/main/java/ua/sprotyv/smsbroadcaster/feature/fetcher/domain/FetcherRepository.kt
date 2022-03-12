package ua.sprotyv.smsbroadcaster.feature.fetcher.domain

import ua.sprotyv.smsbroadcaster.feature.fetcher.domain.entity.Broadcast
import ua.sprotyv.smsbroadcaster.feature.fetcher.domain.entity.Credentials

class FetcherRepository(
    private val credentialsSource: CredentialsSource,
    private val fetcherSource: BroadcastFetcherSource,
) {

    suspend fun getCredentials(): Credentials? =
        credentialsSource.get()

    suspend fun saveCredentials(credentials: Credentials) =
        credentialsSource.put(credentials)


    suspend fun fetchBroadcast(token: String): Broadcast =
        fetcherSource.fetch(token)
}
