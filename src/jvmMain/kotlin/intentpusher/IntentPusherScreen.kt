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
            // region ShowDialogs
            data class DialogInfo(val title: String, val message: String)
            var dialogInfo by remember { mutableStateOf<DialogInfo?>(null) }
            if (dialogInfo != null) {
                ShowDialog(
                    title = dialogInfo?.title.orEmpty(),
                    message = dialogInfo?.message.orEmpty(),
                    onDismissRequest = {
                        viewModel.dismissDialog()
                        dialogInfo = null
                    },
                    onOkButtonClicked = {
                        viewModel.dismissDialog()
                        dialogInfo = null
                    }
                )
            }
            // endregion ShowDialogs

            val state = viewModel.viewStates.collectAsState().value
            if (state is IntentPusherViewState.ShowDialog) {
                dialogInfo = DialogInfo(
                    title = state.title,
                    message = state.message
                )
            }

            LaunchedEffect(true) {
                viewModel.refreshDevices()
            }

            val titleWeight = 0.25f
            val textInputWeight = 1f - titleWeight
            Column {
                TextInputFields(
                    title = "adb Path",
                    textFieldText = {
                        viewModel.inputPath
                    },
                    topPadding = topPadding,
                    endPadding = endPadding,
                    titleWeight = titleWeight,
                    textInputWeight = textInputWeight,
                    onTextFieldValueChanged = {
                        viewModel.updateInputPath(it)
                    }
                )

                TextInputFields(
                    title = "Package Name",
                    textFieldText = {
                        viewModel.inputPackageName
                    },
                    topPadding = topPadding,
                    endPadding = endPadding,
                    titleWeight = titleWeight,
                    textInputWeight = textInputWeight,
                    onTextFieldValueChanged = {
                        viewModel.updatePackageName(it)
                    }
                )

                SpinnerDropdown(
                    title = "Devices",
                    subtitle = "select a device",
                    topPadding = topPadding,
                    endPadding = endPadding,
                    titleWeight = titleWeight,
                    dropdownMenuWeight = textInputWeight,
                    menuitems = viewModel.connectedDevices.toImmutableList(),
                    selectedValue = viewModel.selectedDevice,
                    onDropDownItemSelected = { deviceName ->
                        viewModel.selectedDevice = deviceName
                    }
                )

                TextInputFields(
                    title = "Content",
                    textFieldText = {
                        viewModel.inputContent
                    },
                    topPadding = topPadding,
                    endPadding = endPadding,
                    titleWeight = titleWeight,
                    textInputWeight = textInputWeight,
                    singleLine = false,
                    lines = 3,
                    onTextFieldValueChanged = {
                        viewModel.updateContent(it)
                    }
                )
                ActionButtons(
                    topPadding = topPadding,
                    endPadding = endPadding,
                    onClearButtonClicked = {
                        viewModel.clearFields()
                    },
                    onSendButtonClicked = {
                        viewModel.showSendMessage()
                    }
                )
            }
        }
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
