package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextInputFields(
    title: String,
    textFieldText: () -> String,
    topPadding: Int = 0,
    endPadding: Int = 12,
    titleWeight: Float,
    textInputWeight: Float,
    singleLine: Boolean = true,
    lines: Int = 1,
    onTextFieldValueChanged: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
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
                    .weight(titleWeight)
                    .padding(16.dp)
            )
            TextField(
                value = textFieldText(),
                onValueChange = {
                    onTextFieldValueChanged?.invoke(it)
                },
                singleLine = singleLine,
                maxLines = lines,
                modifier = modifier
                    .align(alignment = Alignment.CenterVertically)
                    .weight(textInputWeight)
                    .fillMaxWidth()
                    .padding(end = endPadding.dp)
            )
        }
    }
}

@Preview
@Composable
fun TextInputFieldsPreview() {
    TextInputFields(
        title = "Hello Compose",
        textFieldText = { "" },
        topPadding = 8,
        endPadding = 8,
        titleWeight = 0.25f,
        textInputWeight = 0.75f
    )
}
