package ua.sprotyv.smsbroadcaster

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.sprotyv.smsbroadcaster.shared.ui.theme.SmsBroadcasterTheme

@Composable
fun MainContent(
    onFetchClick: (token: String) -> Unit,
    fetchInProgress: Boolean,
    smsText: String,
    smsPhones: Int,
    onSendClick: (count: Int) -> Unit,
    onCancelClick: () -> Unit,
    sendInProgress: Boolean,
    sendProgress: Int,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TokenComponent(onFetchClick, fetchInProgress, smsPhones > 0)
        Spacer(Modifier.height(24.dp))
        if (smsText.isNotEmpty() && smsPhones > 0) {
            BroadcastInfo(smsText)
            Spacer(Modifier.height(24.dp))
            SendComponent(sendInProgress, sendProgress, smsPhones, onSendClick, onCancelClick)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TokenComponent(onFetchClick: (token: String) -> Unit, fetchInProgress: Boolean, redownload: Boolean) {
    var token by rememberSaveable { mutableStateOf("") }
    var tokenError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = token,
        onValueChange = { token = it; tokenError = false },
        label = { Text(stringResource(R.string.token_input_label)) },
        maxLines = 2,
        readOnly = fetchInProgress,
        isError = tokenError,

        )
    if (tokenError)
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.error) {
            Text(stringResource(R.string.token_input_error))
        }
    Spacer(modifier = Modifier.height(16.dp))
    Crossfade(targetState = fetchInProgress) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            when (it) {
                true -> CircularProgressIndicator()
                false -> Button(
                    onClick = {
                        if (token.isEmpty()) tokenError = true
                        else {
                            tokenError = false
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            onFetchClick(token)
                        }
                    },
                    content = {
                        Text(
                            stringResource(
                                id = if (redownload) R.string.token_button_again_label else R.string.token_button_label
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun BroadcastInfo(smsText: String) {
    Column(
        Modifier
            .border(width = 1.dp, color = Color.Gray)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = stringResource(R.string.broadcast_sms_label), style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(4.dp))
        Row {
            Text(text = ">")
            Spacer(Modifier.width(8.dp))
            SelectionContainer { Text(text = smsText) }
        }
    }
}

@Composable
private fun SendComponent(
    sendInProgress: Boolean,
    sendProgress: Int,
    sendOverall: Int,
    onSendClick: (count: Int) -> Unit,
    onCancelClick: () -> Unit
) {
    Crossfade(targetState = sendInProgress) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            var sliderPosition by rememberSaveable { mutableStateOf(sendOverall.toFloat()) }
            when (it) {
                false -> {
                    Slider(
                        value = sliderPosition,
                        valueRange = 1f..sendOverall.toFloat(),
                        steps = sendOverall,
                        onValueChange = { sliderPosition = it },
                    )
                    Text(stringResource(R.string.send_slider_value, sliderPosition.toInt()))
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { onSendClick(sliderPosition.toInt()) }) {
                        Text(text = stringResource(id = R.string.send_button_label))
                    }
                }
                true -> {
                    val animatedProgress = animateFloatAsState(
                        targetValue = sendProgress.toFloat() / sendOverall,
                        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
                    )
                    LinearProgressIndicator(progress = animatedProgress.value)
                    Spacer(Modifier.height(4.dp))
                    Text(stringResource(R.string.send_status_label, sendProgress))
                    Spacer(Modifier.height(8.dp))
                    if (sendProgress < sliderPosition) {
                        Button(onClick = onCancelClick) {
                            Text(text = stringResource(R.string.cancel_button_label))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FetchProgressPreview() {
    SmsBroadcasterTheme {
        MainContent(
            onFetchClick = {},
            fetchInProgress = true,
            smsText = "",
            smsPhones = 0,
            onSendClick = {},
            onCancelClick = {},
            sendInProgress = false,
            sendProgress = 0,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReadyToSendPreview() {
    SmsBroadcasterTheme {
        MainContent(
            onFetchClick = {},
            fetchInProgress = false,
            smsText = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
            smsPhones = 101,
            onSendClick = {},
            onCancelClick = {},
            sendInProgress = false,
            sendProgress = 0,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SendProgressPreview() {
    SmsBroadcasterTheme {
        MainContent(
            onFetchClick = {},
            fetchInProgress = false,
            smsText = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
            smsPhones = 101,
            onSendClick = {},
            onCancelClick = {},
            sendInProgress = true,
            sendProgress = 33,
        )
    }
}
