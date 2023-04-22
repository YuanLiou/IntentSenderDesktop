package shellcommands

import container.SimpleResult
import container.TaskResult
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import utils.OsPlatform
import utils.StreamGobbler
import utils.SystemChecker

fun main() {
    val shellCommandExecutor = ShellCommandExecutor(SystemChecker())
    shellCommandExecutor.sendDeeplink(inputPackageName = "", inputContent = "https://taiwan-ebook-lover.github.io/searches/mA8m91o8ylnY9hIGzQDL")
}

class ShellCommandExecutor(
    private val systemChecker: SystemChecker
) {

    fun sendDeeplink(
        inputPath: String = DEFAULT_ADB_PATH,
        inputPackageName: String,
        inputContent: String
    ): SimpleResult<String> {
        if (inputContent.isEmpty()) {
            return TaskResult.Failed(CommandExecutorException("content data is empty."))
        }

        val adbPath = if (inputPath.isBlank()) {
            DEFAULT_ADB_PATH
        } else {
            inputPath
        }

        if (!systemChecker.isMac()) {
            return TaskResult.Failed(CommandExecutorException("Only Support Mac, currently"))
        }

        try {
            val exitCode = executeCommand(adbPath, inputPackageName, inputContent)
            if (exitCode == CODE_COMMAND_NOT_FOUND) {
                return TaskResult.Failed(CommandExecutorException("Command not found."))
            } else if (exitCode == CODE_EXITED_WITH_SOME_ERROR) {
                return TaskResult.Failed(CommandExecutorException("Exited with some error."))
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            if (ioException.message?.contains("error=2") == true) {
                return TaskResult.Failed(CommandExecutorException("Cannot run program \"$adbPath\": No such file or directory"))
            }
            return TaskResult.Failed(ioException)
        }

        return TaskResult.Success("Intent has sent")
    }

    @Throws(IOException::class)
    private fun executeCommand(
        adbPath: String,
        packageName: String,
        content: String
    ): Int {
        val builder = buildAdbProcess(adbPath, packageName, content)
        println("command: ${builder.command().joinToString(" ")}")
        
        val process = builder.start()
        val streamGobbler = StreamGobbler(process.inputStream) {
            println(it)
        }
        val future = Executors.newSingleThreadExecutor().submit(streamGobbler)
        // exitCode == 0, means we sent a success command
        val exitCode = process.waitFor()
        future.get(10, TimeUnit.SECONDS)
        println("Exit code is $exitCode")
        return exitCode
    }

    private fun buildAdbProcess(
        adbPath: String,
        packageName: String,
        content: String
    ): ProcessBuilder {
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

        val processBuilder = ProcessBuilder()
        processBuilder.command(
            "sh",
            "-c",
            adbCommands.joinToString(" "),
        )
        return processBuilder
    }

    class CommandExecutorException(message: String) : Throwable(message)

    companion object {
        private const val DEFAULT_ADB_PATH = "~/Library/Android/sdk/platform-tools/adb"
        private const val CODE_EXITED_WITH_SOME_ERROR = 1
        private const val CODE_COMMAND_NOT_FOUND = 127
    }
}