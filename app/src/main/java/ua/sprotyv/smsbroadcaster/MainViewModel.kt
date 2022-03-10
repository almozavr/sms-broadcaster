package ua.sprotyv.smsbroadcaster

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ua.sprotyv.smsbroadcaster.feature.fetcher.domain.FetcherRepository
import ua.sprotyv.smsbroadcaster.shared.exception.domain.ExceptionHandler
import ua.sprotyv.smsbroadcaster.shared.exception.domain.asCoroutineExceptionHandler
import ua.sprotyv.smsbroadcaster.shared.viewmodel.executeWithHandler

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    exceptionHandler: ExceptionHandler,
    private val fetcherRepository: FetcherRepository,
) : ViewModel(),
    ContainerHost<MainState, MainEffect> {

    companion object {
        private val initialState = MainState(
            fetchInProgress = false,
            smsBody = "",
            phoneNumbers = emptyList(),
            sendInProgress = false,
            sendNumbers = 0,
        )
    }

    override val container: Container<MainState, MainEffect> = container(
        initialState = initialState,
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(exceptionHandler = exceptionHandler.asCoroutineExceptionHandler())
    ) {
        /* initialize */
    }

    fun onFetchClick(token: String) = intent {
        reduce { initialState.copy(fetchInProgress = true) }
        executeWithHandler { fetcherRepository.fetchBroadcast(token) }
            .onSuccess {
                reduce {
                    state.copy(smsBody = it.smsBody, phoneNumbers = it.phones)
                }
            }
        reduce { state.copy(fetchInProgress = false) }
    }

    fun onSendClick(count: Int) = intent {

    }

    fun onCancelClick() = intent {

    }

}
