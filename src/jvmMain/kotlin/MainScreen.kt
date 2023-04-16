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
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import intentpusher.IntentPusherViewModel

@Composable
@Preview
fun MainScreen(
    viewModel: IntentPusherViewModel,
    topPadding: Int = 0,
    endPadding: Int = 0
) {
    MaterialTheme {
        Surface(
            modifier = Modifier.padding(4.dp)
        ) {
            // region ShowDialogs
            var showDialog by remember { mutableStateOf(false) }
            if (showDialog) {
                ShowDialog(
                    onDismissRequest = {
                        showDialog = false
                    },
                    onOkButtonClicked = {
                        showDialog = false
                    }
                )
            }
            // endregion ShowDialogs

            Column {
                TextInputFields(
                    title = "adb Path",
                    textFieldText = {
                        viewModel.inputPath
                    },
                    topPadding = topPadding,
                    endPadding = endPadding,
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
                    onTextFieldValueChanged = {
                        viewModel.updatePackageName(it)
                    }
                )
                TextInputFields(
                    title = "Content",
                    textFieldText = {
                        viewModel.inputContent
                    },
                    topPadding = topPadding,
                    endPadding = endPadding,
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
                        showDialog = true
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
    onClearButtonClicked: (() -> Unit)? = null,
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

@Composable
private fun TextInputFields(
    title: String,
    textFieldText: () -> String,
    topPadding: Int = 0,
    endPadding: Int = 12,
    onTextFieldValueChanged: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(top = topPadding.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                title,
                modifier = modifier.align(alignment = Alignment.CenterVertically)
                    .padding(16.dp)
            )
            TextField(
                value = textFieldText(),
                onValueChange = {
                    onTextFieldValueChanged?.invoke(it)
                },
                singleLine = true,
                modifier = modifier
                    .align(alignment = Alignment.CenterVertically)
                    .fillMaxWidth()
                    .padding(end = endPadding.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowDialog(
    onDismissRequest: (() -> Unit)? = null,
    onOkButtonClicked: (() -> Unit)? = null
) {
    AlertDialog(
        title = {
            Text("Title")
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

