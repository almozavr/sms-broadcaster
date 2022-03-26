package rip.zarmy.smsbroadcaster.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rip.zarmy.smsbroadcaster.MainViewModel

val mainModule = module {
    viewModel {
        MainViewModel(
            savedStateHandle = get(),
            exceptionHandler = get(),
            fetcherRepository = get(),
            smsRepository = get(),
            permissionService = get(),
        )
    }
}
