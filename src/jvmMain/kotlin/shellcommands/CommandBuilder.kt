package shellcommands

import utils.OsPlatform
import utils.SystemChecker

class CommandBuilder(
    private val systemChecker: SystemChecker
) {

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

        val exetutorCommand = lookUpExetutorCommand().toMutableList().apply {
            add(adbCommands.joinToString(" "))
        }
        return AdbCommandExecutor.Command(exetutorCommand)
    }

    private fun lookUpExetutorCommand(): List<String> {
        return when (systemChecker.checkSystem()) {
            OsPlatform.MAC, OsPlatform.LINUX -> listOf("sh", "-c")
            OsPlatform.WINDOWS -> listOf("cmd.exe", "/c")
            OsPlatform.OTHER -> emptyList()
        }
    }
}