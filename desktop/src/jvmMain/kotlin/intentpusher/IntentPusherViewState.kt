package intentpusher

sealed class IntentPusherViewState {
    data class ShowDialog(
        val title: String,
        val message: String
    ) : IntentPusherViewState()

    object WaitForUserInput : IntentPusherViewState()
}
