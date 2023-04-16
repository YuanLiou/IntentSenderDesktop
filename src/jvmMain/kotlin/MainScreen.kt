import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
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

@Composable
@Preview
fun MainScreen() {
    var inputPath = ""
    var inputPackage = ""
    var inputContent = ""
    val topPadding = 16
    val endPadding = 12

    MaterialTheme {
        Surface(
            modifier = Modifier.padding(4.dp)
        ) {
            Column {
                TextInputFields(
                    title = "adb Path",
                    topPadding = topPadding,
                    endPadding = endPadding
                ) {
                    inputPackage = it
                }
                TextInputFields(
                    title = "Package Name",
                    topPadding = topPadding,
                    endPadding = endPadding
                ) {
                    inputPackage = it
                }
                TextInputFields(
                    title = "Content",
                    topPadding = topPadding,
                    endPadding = endPadding
                ) {
                    inputContent = it
                }
                ActionButtons(
                    topPadding = topPadding,
                    endPadding = endPadding,
                    onClearButtonClicked = {
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
    topPadding: Int = 0,
    endPadding: Int = 12,
    modifier: Modifier = Modifier,
    onTextFieldChanged: (String) -> Unit,
) {
    var inputValue by remember { mutableStateOf("") }

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
                value = inputValue,
                onValueChange = {
                    inputValue = it
                    onTextFieldChanged(it)
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

