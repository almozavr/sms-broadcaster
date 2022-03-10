package ua.sprotyv.smsbroadcaster

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ua.sprotyv.smsbroadcaster.feature.fetcher.domain.FetcherRepository
import ua.sprotyv.smsbroadcaster.feature.permission.domain.PermissionService
import ua.sprotyv.smsbroadcaster.feature.sender.domain.SmsRepository
import ua.sprotyv.smsbroadcaster.shared.entity.Status
import ua.sprotyv.smsbroadcaster.shared.exception.domain.ExceptionHandler
import ua.sprotyv.smsbroadcaster.shared.exception.domain.asCoroutineExceptionHandler
import ua.sprotyv.smsbroadcaster.shared.viewmodel.executeWithHandler

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    exceptionHandler: ExceptionHandler,
    private val fetcherRepository: FetcherRepository,
    private val smsRepository: SmsRepository,
    private val permissionService: PermissionService,
) : ViewModel(),
    ContainerHost<MainState, MainEffect> {

    companion object {
        private val initialState = MainState(
            fetchStatus = Status.IDLE,
            smsBody = "",
            phoneNumbers = emptyList(),
            sendStatus = Status.IDLE,
            sendNumbers = 0,
        )
    }

    override val container: Container<MainState, MainEffect> = container(
        initialState = initialState,
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(exceptionHandler = exceptionHandler.asCoroutineExceptionHandler())
    ) {
        observeSmsSender()
    }

    private fun observeSmsSender() = intent {
        smsRepository.connect().collect {
            reduce {
                state.copy(sendNumbers = it.sent, phoneNumbers = it.phones, smsBody = it.body, sendStatus = it.status)
            }
        }
    }

    fun onFetchClick(token: String) = intent {
        reduce { initialState.copy(fetchStatus = Status.PROGRESS) }
        executeWithHandler { fetcherRepository.fetchBroadcast(token) }
            .onSuccess {
                reduce {
                    state.copy(
                        fetchStatus = Status.COMPLETE,
                        smsBody = it.smsBody,
                        phoneNumbers = it.phones
                    )
                }
            }
            .onFailure {
                reduce { state.copy(fetchStatus = Status.IDLE) }
            }
    }

    fun onSendClick(count: Int) = intent {
        val permissionGranted = permissionService.checkSendSms()
        if (permissionGranted.not()) return@intent

        reduce { state.copy(sendStatus = Status.PROGRESS) }
        smsRepository.send(state.smsBody, phones = state.phoneNumbers.subList(0, count))
        reduce { state.copy(sendStatus = Status.COMPLETE) }
    }

    fun onCancelClick() = intent {
        smsRepository.cancel()
        reduce { state.copy(sendStatus = Status.COMPLETE) }
    }

}
