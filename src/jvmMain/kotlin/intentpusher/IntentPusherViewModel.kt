package intentpusher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import shellcommands.AdbCommandExecutor
import shellcommands.AdbPathHelper
import shellcommands.CommandBuilder
import usecase.GetDevices
import usecase.SendDeepLink
import utils.DeviceInfoParser
import utils.SystemChecker

class IntentPusherViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val sendDeepLink: SendDeepLink,
    private val getDevices: GetDevices,
    private val adbPathHelper: AdbPathHelper
) {

    private val mainScope = CoroutineScope(defaultDispatcher)

    private val defaultAdbPath: String
        get() = adbPathHelper.lookUpAdbPath()

    private val _viewStates = MutableStateFlow<IntentPusherViewState>(IntentPusherViewState.WaitForUserInput)
    val viewStates: StateFlow<IntentPusherViewState>
        get() = _viewStates.asStateFlow()

    var inputPath by mutableStateOf(defaultAdbPath)
        private set

    var inputPackageName by mutableStateOf("")
        private set

    var inputContent by mutableStateOf("")
        private set

    var connectedDevices by mutableStateOf(listOf<String>())
        private set
    var selectedDevice: String? by mutableStateOf(null)

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
        inputPath = defaultAdbPath
        inputPackageName = ""
        inputContent = ""
    }

    fun showSendMessage() {
        if (inputContent.isBlank()) {
            showDialog(ERROR_TITLE, "input content is empty")
            return
        }

        if (selectedDevice.isNullOrEmpty()) {
            showDialog(ERROR_TITLE, "no devices connected")
            return
        }

        mainScope.launch {
            sendDeepLink(
                inputPath,
                selectedDevice,
                inputPackageName,
                inputContent
            ).fold(
                onSuccess = {
                    if (it.output.isNotEmpty()) {
                        showDialog("Success", "Intent has sent")
                    }
                },
                onFailure = {
                    val errorMessage = it.message
                    if (errorMessage?.isNotEmpty() == true) {
                        showDialog(ERROR_TITLE, errorMessage)

                        if (errorMessage.contains("device") && errorMessage.contains("not found")) {
                            refreshDevices()
                        }
                    }
                }
            )
        }
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

    fun refreshDevices() {
        mainScope.launch {
            getDevices(inputPath).fold(
                onSuccess = { devices ->
                    if (devices.isEmpty()) {
                        selectedDevice = null
                    }

                    devices.firstOrNull()?.let { firstDevice ->
                        if (!devices.contains(selectedDevice)) {
                            selectedDevice = firstDevice
                        }
                    }
                    connectedDevices = devices
                },
                onFailure = {
                    val errorMessage = it.message
                    if (errorMessage?.isNotEmpty() == true) {
                        showDialog(ERROR_TITLE, errorMessage)
                    }
                }
            )
        }
    }

    companion object {
        private const val ERROR_TITLE = "Error"

        fun create() = IntentPusherViewModel(
            sendDeepLink = SendDeepLink(
                AdbCommandExecutor(),
                CommandBuilder(
                    SystemChecker(),
                    AdbPathHelper(SystemChecker())
                )
            ),
            getDevices = GetDevices(
                AdbCommandExecutor(),
                CommandBuilder(
                    SystemChecker(),
                    AdbPathHelper(SystemChecker())
                ),
                DeviceInfoParser(SystemChecker())
            ),
            adbPathHelper = AdbPathHelper(SystemChecker())
        )
    }
}
