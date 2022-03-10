package ua.sprotyv.smsbroadcaster.feature.fetcher.data

import ua.sprotyv.smsbroadcaster.feature.fetcher.domain.BroadcastFetcherSource
import ua.sprotyv.smsbroadcaster.feature.fetcher.domain.entity.Broadcast

class BroadcastFetcherApiSource(
    private val apiService: BroadcastApiService,
) : BroadcastFetcherSource {

    override suspend fun fetch(token: String): Broadcast =
        apiService.fetchBroadcast("Bearer: $token").asDomain()

}
