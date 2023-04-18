package intentpusher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class IntentPusherViewModel {

    private val _viewStates = MutableStateFlow<IntentPusherViewState?>(null)
    val viewStates: StateFlow<IntentPusherViewState?>
        get() = _viewStates.asStateFlow()

    var inputPath by mutableStateOf("")
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
        inputPath = ""
        inputPackageName = ""
        inputContent = ""
    }

    fun showSendMessage() {
        _viewStates.value = IntentPusherViewState.ShowDialog(
            title = "Sample Dialog",
            message = "Hello world"
        )
    }

    fun dismissDialog() {
        _viewStates.value = IntentPusherViewState.WaitForUserInput
    }
}