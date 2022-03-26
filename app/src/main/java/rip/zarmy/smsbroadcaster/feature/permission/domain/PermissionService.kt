package rip.zarmy.smsbroadcaster.feature.permission.domain

interface PermissionService {
    suspend fun checkSendSms(): Boolean
}
