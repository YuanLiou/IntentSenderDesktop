package intentpusher

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ui.SpinnerDropdown
import ui.TextInputFields

@Composable
fun IntentPusherScreen(
    viewModel: IntentPusherViewModel,
    topPadding: Int = 0,
    endPadding: Int = 0,
    modifier: Modifier = Modifier
) {
    MaterialTheme {
        Surface(
            modifier = modifier.padding(4.dp)
        ) {
            PopupDialogs(
                viewState = viewModel.viewStates.collectAsState().value,
                onDismissRequest = {
                    viewModel.dismissDialog()
                },
                onOkButtonClicked = {
                    viewModel.dismissDialog()
                }
            )

            LaunchedEffect(true) {
                viewModel.refreshDevices()
            }

            IntentInfoInput(
                inputAdbPath = viewModel.inputPath,
                onAdbPathValueChanged = {
                    viewModel.updateInputPath(it)
                },
                inputPackageName = viewModel.inputPackageName,
                onPackageNameValueChanged = {
                    viewModel.updatePackageName(it)
                },
                connectedDevices = viewModel.connectedDevices.toImmutableList(),
                selectedDevice = viewModel.selectedDevice.orEmpty(),
                onDropDownItemSelected = { deviceName ->
                    viewModel.selectedDevice = deviceName
                },
                onRefreshButtonClicked = {
                    viewModel.refreshDevices()
                },
                inputContent = viewModel.inputContent,
                onInputContentValueChanged = {
                    viewModel.updateContent(it)
                },
                onSendButtonClicked = {
                    viewModel.showSendMessage()
                },
                onClearButtonClicked = {
                    viewModel.clearFields()
                },
                topPadding,
                endPadding,
                modifier
            )
        }
    }
}

@Composable
private fun IntentInfoInput(
    inputAdbPath: String,
    onAdbPathValueChanged: ((String) -> Unit)? = null,
    inputPackageName: String,
    onPackageNameValueChanged: ((String) -> Unit)? = null,
    connectedDevices: ImmutableList<String>,
    selectedDevice: String,
    onDropDownItemSelected: ((String) -> Unit)? = null,
    onRefreshButtonClicked: (() -> Unit)? = null,
    inputContent: String,
    onInputContentValueChanged: ((String) -> Unit)? = null,
    onSendButtonClicked: (() -> Unit)? = null,
    onClearButtonClicked: (() -> Unit)? = null,
    topPadding: Int,
    endPadding: Int,
    modifier: Modifier
) {
    val titleWeight = 0.25f
    val textInputWeight = 1f - titleWeight
    Column {
        TextInputFields(
            title = "adb Path",
            textFieldText = { inputAdbPath },
            topPadding = topPadding,
            endPadding = endPadding,
            titleWeight = titleWeight,
            textInputWeight = textInputWeight,
            onTextFieldValueChanged = {
                onAdbPathValueChanged?.invoke(it)
            }
        )

        TextInputFields(
            title = "Package Name",
            textFieldText = { inputPackageName },
            topPadding = topPadding,
            endPadding = endPadding,
            titleWeight = titleWeight,
            textInputWeight = textInputWeight,
            onTextFieldValueChanged = {
                onPackageNameValueChanged?.invoke(it)
            }
        )

        SpinnerDropdown(
            title = "Devices",
            subtitle = "select a device",
            topPadding = topPadding,
            endPadding = endPadding,
            titleWeight = titleWeight,
            dropdownMenuWeight = textInputWeight,
            menuitems = connectedDevices,
            selectedValue = selectedDevice,
            onDropDownItemSelected = { deviceName ->
                onDropDownItemSelected?.invoke(deviceName)
            },
            onRefreshButtonClicked = {
                onRefreshButtonClicked?.invoke()
            },
            modifier = modifier
        )

        TextInputFields(
            title = "Content",
            textFieldText = { inputContent },
            topPadding = topPadding,
            endPadding = endPadding,
            titleWeight = titleWeight,
            textInputWeight = textInputWeight,
            singleLine = false,
            lines = 3,
            onTextFieldValueChanged = {
                onInputContentValueChanged?.invoke(it)
            }
        )
        ActionButtons(
            topPadding = topPadding,
            endPadding = endPadding,
            onClearButtonClicked = {
                onClearButtonClicked?.invoke()
            },
            onSendButtonClicked = {
                onSendButtonClicked?.invoke()
            }
        )
    }
}

@Composable
private fun PopupDialogs(
    viewState: IntentPusherViewState,
    onDismissRequest: (() -> Unit)? = null,
    onOkButtonClicked: (() -> Unit)? = null
) {
    // region ShowDialogs
    data class DialogInfo(val title: String, val message: String)

    var dialogInfo by remember { mutableStateOf<DialogInfo?>(null) }
    if (dialogInfo != null) {
        ShowDialog(
            title = dialogInfo?.title.orEmpty(),
            message = dialogInfo?.message.orEmpty(),
            onDismissRequest = {
                onDismissRequest?.invoke()
                dialogInfo = null
            },
            onOkButtonClicked = {
                onOkButtonClicked?.invoke()
                dialogInfo = null
            }
        )
    }
    // endregion ShowDialogs
    if (viewState is IntentPusherViewState.ShowDialog) {
        dialogInfo = DialogInfo(
            title = viewState.title,
            message = viewState.message
        )
    }
}

@Composable
private fun ActionButtons(
    topPadding: Int = 0,
    endPadding: Int = 0,
    modifier: Modifier = Modifier,
    onSendButtonClicked: (() -> Unit)? = null,
    onClearButtonClicked: (() -> Unit)? = null
) {
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier.fillMaxWidth()
            .padding(top = topPadding.dp, end = endPadding.dp)
    ) {
        Row {
            Button(onClick = {
                onClearButtonClicked?.invoke()
            }) {
                Text("Clear")
            }
            Spacer(modifier = modifier.padding(4.dp))
            Button(onClick = {
                onSendButtonClicked?.invoke()
            }) {
                Text("Send")
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowDialog(
    title: String,
    message: String,
    onDismissRequest: (() -> Unit)? = null,
    onOkButtonClicked: (() -> Unit)? = null
) {
    AlertDialog(
        title = {
            Text(title)
        },
        text = {
            Text(message)
        },
        confirmButton = {
            Button(
                onClick = {
                    onOkButtonClicked?.invoke()
                }
            ) {
                Text("OK")
            }
        },
        onDismissRequest = {
            onDismissRequest?.invoke()
        }
    )
}

@Composable
@Preview()
fun IntentPusherScreenPreview() {
    MaterialTheme {
        IntentPusherScreen(
            IntentPusherViewModel.create(),
            topPadding = 16,
            endPadding = 12
        )
    }
}

@Preview
@Composable
fun ActionButtonsPreview() {
    ActionButtons(
        topPadding = 4,
        endPadding = 4
    )
}
