package shellcommands

import container.SimpleResult
import container.TaskResult
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import utils.StreamGobbler
import utils.SystemChecker

class AdbCommandExecutor(
    private val systemChecker: SystemChecker,
    val commandBuilder: CommandBuilder
) {

    fun sendDeeplink(
        inputPath: String = commandBuilder.lookUpAdbPath(),
        inputPackageName: String,
        inputContent: String
    ): SimpleResult<String> {
        if (inputContent.isEmpty()) {
            return TaskResult.Failed(CommandExecutorException("content data is empty."))
        }

        if (!systemChecker.isMac()) {
            return TaskResult.Failed(CommandExecutorException("Only Support Mac, currently"))
        }

        val deepLinkCommand = commandBuilder.buildCommand(
            inputPath,
            inputPackageName,
            inputContent
        )

        try {
            val exitCode = executeCommand(deepLinkCommand)
            if (exitCode == CODE_COMMAND_NOT_FOUND) {
                return TaskResult.Failed(CommandExecutorException("Command not found."))
            } else if (exitCode == CODE_EXITED_WITH_SOME_ERROR) {
                return TaskResult.Failed(CommandExecutorException("Exited with some error."))
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            if (ioException.message?.contains("error=2") == true) {
                val adbPath = commandBuilder.lookUpAdbPath()
                return TaskResult.Failed(CommandExecutorException("Cannot run program \"$adbPath\": No such file or directory"))
            }
            return TaskResult.Failed(ioException)
        }

        return TaskResult.Success("Intent has sent")
    }

    @Throws(IOException::class)
    private fun executeCommand(command: Command): Int {
        val builder = ProcessBuilder()
        builder.command(command.commands)
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

    @JvmInline
    value class Command(val commands: List<String>)
    class CommandExecutorException(message: String) : Throwable(message)

    companion object {
        private const val CODE_EXITED_WITH_SOME_ERROR = 1
        private const val CODE_COMMAND_NOT_FOUND = 127
    }
}