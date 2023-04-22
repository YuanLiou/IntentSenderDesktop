package shellcommands

class CommandBuilder {

    fun buildCommand(
        adbPath: String,
        packageName: String,
        content: String
    ): AdbCommandExecutor.Command {
        // sample: adb shell am start -a android.intent.action.VIEW -d "your-link" com.myapp
        val shellCommands = mutableListOf(
            "am",
            "start",
            "-a",
            "android.intent.action.VIEW",
            "-d",
            "\"$content\"",
        )

        if (packageName.isNotBlank()) {
            shellCommands.add(packageName)
        }

        val adbCommands = listOf(
            adbPath,
            "shell",
            "\'${shellCommands.joinToString(" ")}\'"
        )

        val exetutorCommand = listOf(
            "sh",
            "-c",
            adbCommands.joinToString(" ")
        )

        return AdbCommandExecutor.Command(exetutorCommand)
    }
}