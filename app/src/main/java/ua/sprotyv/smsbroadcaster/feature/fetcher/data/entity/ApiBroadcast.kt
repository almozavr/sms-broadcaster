package ua.sprotyv.smsbroadcaster.feature.fetcher.data.entity

import ua.sprotyv.smsbroadcaster.feature.fetcher.domain.entity.Broadcast

data class ApiBroadcast(
    val sms_body: String,
    val phones: List<String>
) {
    fun asDomain() = Broadcast(smsBody = sms_body, phones = phones)
}
