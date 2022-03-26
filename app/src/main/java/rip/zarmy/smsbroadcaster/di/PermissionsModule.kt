package rip.zarmy.smsbroadcaster.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import rip.zarmy.smsbroadcaster.feature.permission.data.WarderPermissionService
import rip.zarmy.smsbroadcaster.feature.permission.domain.PermissionService

val permissionsModule = module {
    factory<PermissionService> { WarderPermissionService(androidContext()) }
}
