package ua.sprotyv.smsbroadcaster.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.sprotyv.smsbroadcaster.MainViewModel

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
