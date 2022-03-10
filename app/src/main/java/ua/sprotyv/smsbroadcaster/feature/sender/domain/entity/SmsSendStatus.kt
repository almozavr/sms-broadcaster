package ua.sprotyv.smsbroadcaster.feature.sender.domain.entity

import ua.sprotyv.smsbroadcaster.shared.entity.Status

data class SmsSendStatus(
    val body: String,
    val phones: List<String>,
    val sent: Int,
    val status: Status
)
