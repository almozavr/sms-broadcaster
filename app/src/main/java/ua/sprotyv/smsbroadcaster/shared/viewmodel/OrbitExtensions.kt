package ua.sprotyv.smsbroadcaster.shared.viewmodel

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.OrbitDsl
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent

val <STATE : Any> ContainerHost<STATE, *>.currentState: STATE
    get() = container.stateFlow.value

/**
 * Collect [ContainerHost.container]'s state via [collectAsState] but bounded with the [flowWithLifecycle]
 */
@Composable
fun <STATE : Any, SIDE_EFFECT : Any> ContainerHost<STATE, SIDE_EFFECT>.composableState(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): State<STATE> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleAwareStateFlow = remember(this, lifecycleOwner) {
        container.stateFlow.flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)
    }
    return lifecycleAwareStateFlow.collectAsState(currentState)
}

/**
 * Collect [ContainerHost.container]'s effect bounded with the [flowWithLifecycle]
 */
@OptIn(InternalCoroutinesApi::class)
@Composable
fun <STATE : Any, SIDE_EFFECT : Any> ContainerHost<STATE, SIDE_EFFECT>.composableEffect(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    action: FlowCollector<SIDE_EFFECT>,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(this, lifecycleOwner) {
        launch {
            container.sideEffectFlow.flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState).collect(action)
        }
    }
}
