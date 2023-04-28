import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import intentpusher.IntentPusherViewModel
import intentpusher.IntentPusherScreen
import shellcommands.CommandBuilder
import shellcommands.AdbCommandExecutor
import shellcommands.AdbPathHelper
import usecase.SendDeepLink
import utils.SystemChecker

fun main() = application {
    Window(
        title = "Intent Sender",
        onCloseRequest = ::exitApplication
    ) {
        IntentPusherScreen(
            viewModel = IntentPusherViewModel.create(),
            topPadding = 16,
            endPadding = 12
        )
    }
}


