package rip.zarmy.smsbroadcaster.feature.sender.domain.entity

import rip.zarmy.smsbroadcaster.shared.entity.Status

data class SmsSendStatus(
    val body: String,
    val phones: List<String>,
    val sent: Int,
    val status: Status
)
