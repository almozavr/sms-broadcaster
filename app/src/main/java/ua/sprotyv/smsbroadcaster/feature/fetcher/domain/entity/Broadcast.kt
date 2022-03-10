package ua.sprotyv.smsbroadcaster.feature.fetcher.domain.entity

data class Broadcast(
    val smsBody: String,
    val phones: List<String>
)
