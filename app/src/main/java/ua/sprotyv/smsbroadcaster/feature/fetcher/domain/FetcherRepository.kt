package ua.sprotyv.smsbroadcaster.feature.fetcher.domain

import ua.sprotyv.smsbroadcaster.feature.fetcher.domain.entity.Broadcast

class FetcherRepository(private val source: BroadcastFetcherSource) {

    suspend fun fetchBroadcast(token: String): Broadcast =
        source.fetch(token)
}

interface BroadcastFetcherSource {
    suspend fun fetch(token: String): Broadcast
}
