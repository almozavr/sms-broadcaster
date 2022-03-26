package rip.zarmy.smsbroadcaster.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import rip.zarmy.smsbroadcaster.shared.coroutines.executeWithHandler

suspend inline fun <R> ViewModel.executeWithHandler(block: () -> R): Result<R> =
    viewModelScope.executeWithHandler(block)
