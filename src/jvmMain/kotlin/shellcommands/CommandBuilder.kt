package shellcommands

import utils.OsPlatform
import utils.SystemChecker

class CommandBuilder(
    private val systemChecker: SystemChecker,
    private val adbPathHelper: AdbPathHelper
) {

    fun buildDevicesCommand(adbPath: String): Command {
        val adbCommands = buildAdbCommand(
            adbPath = adbPath,
            deviceName = null,
            "devices"
        )
        return buildFinalExecutableCommand(adbCommands)
    }

    fun buildDeepLinkCommand(
        adbPath: String,
        deviceName: String?,
        packageName: String,
        content: String
    ): Command {
        // sample: adb (-s devicename) shell am start -a android.intent.action.VIEW -d "your-link" com.myapp
        val shellCommands = mutableListOf(
            "am",
            "start",
            "-a",
            "android.intent.action.VIEW",
            "-d",
            "\"$content\""
        )

        if (packageName.isNotBlank()) {
            shellCommands.add(packageName)
        }

        val shellParameters = when (systemChecker.checkSystem()) {
            OsPlatform.WINDOWS -> shellCommands.joinToString(" ")
            else -> "\'${shellCommands.joinToString(" ")}\'"
        }

        val adbCommands = buildAdbCommand(
            adbPath = adbPath,
            deviceName = deviceName,
            "shell",
            shellParameters
        )

        return buildFinalExecutableCommand(adbCommands)
    }

    private fun buildFinalExecutableCommand(adbCommands: List<String>): Command {
        val executorCommand = lookUpExecutorCommand().toMutableList().apply {
            add(adbCommands.joinToString(" "))
        }
        return Command(executorCommand)
    }

    private fun buildAdbCommand(
        adbPath: String,
        deviceName: String?,
        vararg adbCommands: String
    ): List<String> {
        val finalCommandList = mutableListOf(adbPathHelper.lookUpAdbPath(inputPath = adbPath))

        if (!deviceName.isNullOrEmpty()) {
            finalCommandList.add("-s")
            finalCommandList.add(deviceName)
        }

        for (command in adbCommands) {
            finalCommandList.add(command)
        }
        return finalCommandList
    }

    private fun lookUpExecutorCommand(): List<String> {
        return when (systemChecker.checkSystem()) {
            OsPlatform.MAC, OsPlatform.LINUX -> listOf("sh", "-c")
            OsPlatform.WINDOWS -> listOf("cmd.exe", "/c")
            OsPlatform.OTHER -> emptyList()
        }
    }
}
