package ua.sprotyv.smsbroadcaster.feature.sender.domain

import kotlinx.coroutines.flow.Flow

interface SmsRepository {
    fun send(body: String, phones: List<String>): Flow<SendResult>
    fun cancel()
}

data class SendResult(val sent: Int)
