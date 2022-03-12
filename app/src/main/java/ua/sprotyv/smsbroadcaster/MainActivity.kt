package ua.sprotyv.smsbroadcaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.sprotyv.smsbroadcaster.shared.ui.theme.SmsBroadcasterTheme
import ua.sprotyv.smsbroadcaster.shared.viewmodel.composableEffect
import ua.sprotyv.smsbroadcaster.shared.viewmodel.composableState

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            RenderEffect(scaffoldState, coroutineScope)
            val state by viewModel.composableState()
            SmsBroadcasterTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) {
                    MainContent(
                        initialToken = state.token,
                        onFetchClick = viewModel::onFetchClick,
                        fetchStatus = state.fetchStatus,
                        smsText = state.smsBody,
                        smsPhones = state.phoneNumbers.size,
                        onSendClick = viewModel::onSendClick,
                        onCancelClick = viewModel::onCancelClick,
                        sendStatus = state.sendStatus,
                        sendProgress = state.sendNumbers,
                    )
                }
            }
        }
    }

    @Composable
    private fun RenderEffect(scaffoldState: ScaffoldState, coroutineScope: CoroutineScope) {
        viewModel.composableEffect { effect ->
            when (effect) {
                is MainEffect.FetchError -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = getString(
                            if (effect.authFail) R.string.fetch_error_auth
                            else R.string.fetch_error_generic
                        )
                    )
                }
            }
        }
    }
}
