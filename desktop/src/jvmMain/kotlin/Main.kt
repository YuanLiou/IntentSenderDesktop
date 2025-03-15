import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import intentpusher.IntentPusherScreen
import intentpusher.IntentPusherViewModel

fun main() =
    application {
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
