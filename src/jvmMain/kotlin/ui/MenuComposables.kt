package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val REFRESH_BUTTON_WEIGHT = 0.1f

@Composable
fun SpinnerDropdown(
    title: String,
    subtitle: String,
    selectedValue: String,
    topPadding: Int = 0,
    endPadding: Int = 12,
    titleWeight: Float,
    dropdownMenuWeight: Float,
    menuitems: ImmutableList<String>,
    onDropDownItemSelected: ((String) -> Unit)? = null,
    onRefreshButtonClicked: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(top = topPadding.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                title,
                modifier = modifier.align(alignment = Alignment.CenterVertically)
                    .weight(titleWeight)
                    .padding(16.dp)
            )

            val boxedSpinnerTitle = if (menuitems.isEmpty()) {
                "Connect a device"
            } else {
                subtitle
            }

            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = modifier
                    .weight(dropdownMenuWeight - REFRESH_BUTTON_WEIGHT)
            ) {
                BoxedDropdownMenu(
                    label = boxedSpinnerTitle,
                    menuitems = menuitems,
                    selectedValue = selectedValue,
                    onDropDownItemSelected = onDropDownItemSelected,
                    modifier = modifier
                )
            }

            Image(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "refresh devices",
                modifier = modifier
                    .weight(REFRESH_BUTTON_WEIGHT)
                    .padding(end = endPadding.dp)
                    .clickable {
                        onRefreshButtonClicked?.invoke()
                    }
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun BoxedDropdownMenu(
    label: String,
    selectedValue: String,
    menuitems: ImmutableList<String>,
    endPadding: Int = 12,
    onDropDownItemSelected: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box {
        var expander by remember { mutableStateOf(false) }
        BoxedInputText(
            label = label,
            selectedValue = selectedValue,
            endPadding = endPadding,
            isExpandDropdownMenu = expander,
            onBoxedShapeClicked = { expander = true },
            isEnabled = menuitems.isNotEmpty(),
            modifier = modifier
        )
        DropdownMenu(
            expanded = expander,
            onDismissRequest = { expander = false },
            modifier = modifier
                .padding(end = endPadding.dp)
        ) {
            menuitems.forEachIndexed { _, itemTitle ->
                DropdownMenuItem(
                    onClick = {
                        onDropDownItemSelected?.invoke(itemTitle)
                        expander = false
                    }
                ) {
                    Text(text = itemTitle)
                }
            }
        }
    }
}

@Composable
private fun BoxedInputText(
    label: String,
    selectedValue: String,
    isExpandDropdownMenu: Boolean,
    endPadding: Int = 0,
    onBoxedShapeClicked: (() -> Unit)? = null,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(end = endPadding.dp)
            .height(IntrinsicSize.Min)
    ) {
        OutlinedTextField(
            label = { Text(text = label) },
            value = selectedValue,
            enabled = isEnabled,
            trailingIcon = {
                val icon = if (isExpandDropdownMenu) {
                    Icons.Filled.KeyboardArrowUp
                } else {
                    Icons.Filled.KeyboardArrowDown
                }
                Icon(icon, "")
            },
            onValueChange = {},
            readOnly = true,
            modifier = modifier.fillMaxWidth()
        )

        // Transparent clickable surface on top of OutlinedTextField
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize()
                .clip(MaterialTheme.shapes.small)
                .clickable(enabled = isEnabled) {
                    onBoxedShapeClicked?.invoke()
                }
        ) {
            // no-op
        }
    }
}

@Preview
@Composable
fun SpinnerDropdownPreview() {
    Box(modifier = Modifier.background(Color.White)) {
        SpinnerDropdown(
            title = "Hello Dropdown",
            subtitle = "devices",
            selectedValue = "",
            topPadding = 4,
            endPadding = 4,
            titleWeight = 0.25f,
            dropdownMenuWeight = 0.75f,
            menuitems = listOf("22141FDEE000TW", "emulator-5554").toImmutableList()
        )
    }
}

@Preview
@Composable
fun IntentSenderDropdownMenuPreview() {
    Box(modifier = Modifier.background(Color.White)) {
        BoxedDropdownMenu(
            label = "Devices",
            selectedValue = "",
            endPadding = 4,
            menuitems = listOf("22141FDEE000TW", "emulator-5554").toImmutableList()
        )
    }
}

@Preview
@Composable
fun BoxedInputTextPreview() {
    Box(modifier = Modifier.background(Color.White)) {
        BoxedInputText(
            label = "Devices",
            selectedValue = "test value",
            isExpandDropdownMenu = false,
            isEnabled = false
        )
    }
}
