package ua.sprotyv.smsbroadcaster

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import ua.sprotyv.smsbroadcaster.shared.exception.domain.ExceptionHandler
import ua.sprotyv.smsbroadcaster.shared.exception.domain.asCoroutineExceptionHandler

class MainViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val exceptionHandler: ExceptionHandler
) : ViewModel(),
    ContainerHost<MainState, MainEffect> {

    override val container: Container<MainState, MainEffect> = container(
        initialState = MainState(
            fetchInProgress = false,
            smsBody = "",
            smsNumbers = 0,
        ),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(exceptionHandler = exceptionHandler.asCoroutineExceptionHandler())
    ) {
        /* initialize */
    }

    fun onFetchClick(token: String) = intent {
        reduce { state.copy(fetchInProgress = true) }
        delay(3000L)
        reduce { state.copy(fetchInProgress = false) }
        reduce { state.copy(smsBody = "Hello Motherfucker", smsNumbers = 101) }
    }

}
