package ua.sprotyv.smsbroadcaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.sprotyv.smsbroadcaster.shared.ui.theme.SmsBroadcasterTheme
import ua.sprotyv.smsbroadcaster.shared.viewmodel.composableState

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.composableState()
            SmsBroadcasterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainContent(
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
}
