package ua.sprotyv.smsbroadcaster

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ua.sprotyv.smsbroadcaster.shared.exception.domain.ExceptionHandler
import ua.sprotyv.smsbroadcaster.shared.exception.domain.asCoroutineExceptionHandler

class MainViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val exceptionHandler: ExceptionHandler
) : ViewModel(),
    ContainerHost<MainState, MainEffect> {

    override val container: Container<MainState, MainEffect> = container(
        initialState = MainState("Test"),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(exceptionHandler = exceptionHandler.asCoroutineExceptionHandler())
    ) {
        /* initialize */
    }

}
