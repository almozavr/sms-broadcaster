package rip.zarmy.smsbroadcaster

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import rip.zarmy.smsbroadcaster.feature.fetcher.domain.FetcherRepository
import rip.zarmy.smsbroadcaster.feature.fetcher.domain.entity.Credentials
import rip.zarmy.smsbroadcaster.feature.permission.domain.PermissionService
import rip.zarmy.smsbroadcaster.feature.sender.domain.SmsRepository
import rip.zarmy.smsbroadcaster.shared.entity.Status
import rip.zarmy.smsbroadcaster.shared.exception.domain.ExceptionHandler
import rip.zarmy.smsbroadcaster.shared.exception.domain.asCoroutineExceptionHandler
import rip.zarmy.smsbroadcaster.shared.viewmodel.executeWithHandler

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
            token = "",
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
        loadCachedToken()
        observeSmsSender()
    }

    private fun loadCachedToken() = intent {
        fetcherRepository.getCredentials()?.also {
            reduce { state.copy(token = it.token) }
        }
    }

    fun onFetchClick(token: String) = intent {
        reduce { initialState.copy(fetchStatus = Status.PROGRESS, token = token) }
        fetcherRepository.saveCredentials(Credentials(token))
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
                postSideEffect(MainEffect.FetchError(authFail = it.message?.contains("403") == true))
            }
    }

    private fun observeSmsSender() = intent {
        smsRepository.observe().collect {
            reduce {
                state.copy(sendNumbers = it.sent, phoneNumbers = it.phones, smsBody = it.body, sendStatus = it.status)
            }
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
