package ua.sprotyv.smsbroadcaster.feature.fetcher.domain

import ua.sprotyv.smsbroadcaster.feature.fetcher.domain.entity.Broadcast

interface BroadcastFetcherSource {
    suspend fun fetch(token: String): Broadcast
}
