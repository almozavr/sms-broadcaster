package rip.zarmy.smsbroadcaster.feature.permission.data

import android.Manifest
import android.content.Context
import com.alexstyl.warden.PermissionState
import com.alexstyl.warden.Warden
import rip.zarmy.smsbroadcaster.feature.permission.domain.PermissionService

class WarderPermissionService(private val context: Context) : PermissionService {

    override suspend fun checkSendSms(): Boolean =
        Warden.with(context).requestPermission(Manifest.permission.SEND_SMS) == PermissionState.Granted
}
