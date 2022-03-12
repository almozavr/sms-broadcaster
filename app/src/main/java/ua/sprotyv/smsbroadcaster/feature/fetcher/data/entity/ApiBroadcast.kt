package ua.sprotyv.smsbroadcaster.feature.fetcher.data.entity

import ua.sprotyv.smsbroadcaster.feature.fetcher.domain.entity.Broadcast

data class ApiBroadcast(
    val text: String,
    val phones: List<String>
) {
    fun asDomain() = Broadcast(smsBody = text, phones = phones)
}
