import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import intentpusher.IntentPusherViewModel
import shellcommands.ShellCommandExecutor
import utils.SystemChecker

fun main() = application {
    Window(
        title = "Intent Sender",
        onCloseRequest = ::exitApplication
    ) {
        MainScreen(
            viewModel = IntentPusherViewModel(
                ShellCommandExecutor(SystemChecker())
            ),
            topPadding = 16,
            endPadding = 12
        )
    }
}
