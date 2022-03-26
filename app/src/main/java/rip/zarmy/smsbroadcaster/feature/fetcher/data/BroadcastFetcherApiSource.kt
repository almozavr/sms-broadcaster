package rip.zarmy.smsbroadcaster.feature.fetcher.data

import rip.zarmy.smsbroadcaster.feature.fetcher.domain.BroadcastFetcherSource
import rip.zarmy.smsbroadcaster.feature.fetcher.domain.entity.Broadcast

class BroadcastFetcherApiSource(
    private val apiService: BroadcastApiService,
) : BroadcastFetcherSource {

    override suspend fun fetch(token: String): Broadcast =
        apiService.fetchBroadcast(token).asDomain()
}
