package rip.zarmy.smsbroadcaster.feature.fetcher.domain

import rip.zarmy.smsbroadcaster.feature.fetcher.domain.entity.Credentials

interface CredentialsSource {
    suspend fun get(): Credentials?
    suspend fun put(credentials: Credentials)
}
