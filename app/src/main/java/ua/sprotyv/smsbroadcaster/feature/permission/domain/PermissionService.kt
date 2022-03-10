package ua.sprotyv.smsbroadcaster.feature.permission.domain

interface PermissionService {
    suspend fun checkSendSms(): Boolean
}
