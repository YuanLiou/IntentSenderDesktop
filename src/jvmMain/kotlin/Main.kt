import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import intentpusher.IntentPusherViewModel
import shellcommands.CommandBuilder
import shellcommands.AdbCommandExecutor
import utils.SystemChecker

fun main() = application {
    Window(
        title = "Intent Sender",
        onCloseRequest = ::exitApplication
    ) {
        MainScreen(
            viewModel = IntentPusherViewModel(
                AdbCommandExecutor(
                    SystemChecker(),
                    CommandBuilder(SystemChecker())
                )
            ),
            topPadding = 16,
            endPadding = 12
        )
    }
}
