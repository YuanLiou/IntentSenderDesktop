package intentpusher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import shellcommands.AdbCommandExecutor

class IntentPusherViewModel(
    private val adbCommandExecutor: AdbCommandExecutor
) {

    private val _viewStates = MutableStateFlow<IntentPusherViewState>(IntentPusherViewState.WaitForUserInput)
    val viewStates: StateFlow<IntentPusherViewState>
        get() = _viewStates.asStateFlow()

    var inputPath by mutableStateOf(AdbCommandExecutor.DEFAULT_ADB_PATH)
        private set

    var inputPackageName by mutableStateOf("")
        private set

    var inputContent by mutableStateOf("")
        private set

    fun updateInputPath(inputPath: String) {
        this.inputPath = inputPath
    }

    fun updatePackageName(packageName: String) {
        this.inputPackageName = packageName
    }

    fun updateContent(content: String) {
        this.inputContent = content
    }

    fun clearFields() {
        inputPath = AdbCommandExecutor.DEFAULT_ADB_PATH
        inputPackageName = ""
        inputContent = ""
    }

    fun showSendMessage() {
        if (inputContent.isBlank()) {
            showDialog(ERROR_TITLE, "input content is empty")
            return
        }

        adbCommandExecutor.sendDeeplink(
            inputPath,
            inputPackageName,
            inputContent
        ).fold(
            onSuccess = {
                showDialog("Success", "Message is $it")
            },
            onFailure = {
                val errorMessage = it.message
                if (errorMessage?.isNotEmpty() == true) {
                    showDialog(ERROR_TITLE, errorMessage)
                }
            }
        )
    }

    fun dismissDialog() {
        _viewStates.value = IntentPusherViewState.WaitForUserInput
    }

    private fun showDialog(title: String, message: String) {
        _viewStates.value = IntentPusherViewState.ShowDialog(
            title = title,
            message = message
        )
    }

    companion object {
        private const val ERROR_TITLE = "Error"
    }
}