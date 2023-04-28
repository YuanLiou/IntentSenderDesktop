package shellcommands

import container.SimpleResult
import container.TaskResult
import java.io.IOException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class AdbCommandExecutor {

    suspend fun executeCommand(command: Command, program: String?): CommandResult {
        return try {
            val result = execute(command)
            if (result.exitCode == CODE_COMMAND_NOT_FOUND) {
                throw CommandExecutorException("Command not found.")
            } else if (result.exitCode == CODE_EXITED_WITH_SOME_ERROR) {
                throw CommandExecutorException("Exited with some error.")
            }

            result
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            if (ioException.message?.contains("error=2") == true) {
                throw CommandExecutorException("Cannot run program \"${program.orEmpty()}\": No such file or directory")
            }
            throw ioException
        }
    }

    @Throws(IOException::class)
    private suspend fun execute(command: Command): CommandResult {
        val builder = ProcessBuilder()
        builder.command(command.commands)
        println("command: ${builder.command().joinToString(" ")}")
        val process = builder.start()
        return process.gobbleStream()
    }

    /*
     * @return exitCode: Int, output: String, error: String
     */
    private suspend fun Process.gobbleStream() = coroutineScope {
        val output = async { inputStream.bufferedReader().use { it.readText() } }
        val error = async { errorStream.bufferedReader().use { it.readText() } }
        val exitCode = waitFor()
        CommandResult(exitCode, output.await(), error.await())
    }
    @JvmInline
    value class Command(val commands: List<String>)
    class CommandExecutorException(message: String) : Throwable(message)

    companion object {
        private const val CODE_EXITED_WITH_SOME_ERROR = 1
        private const val CODE_COMMAND_NOT_FOUND = 127
    }
}