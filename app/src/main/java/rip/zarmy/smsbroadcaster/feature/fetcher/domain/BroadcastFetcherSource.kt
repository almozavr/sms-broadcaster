package rip.zarmy.smsbroadcaster.feature.fetcher.domain

import rip.zarmy.smsbroadcaster.feature.fetcher.domain.entity.Broadcast

interface BroadcastFetcherSource {
    suspend fun fetch(token: String): Broadcast
}
