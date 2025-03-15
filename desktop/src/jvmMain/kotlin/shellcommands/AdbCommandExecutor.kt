package shellcommands

import java.io.IOException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class AdbCommandExecutor {
    suspend fun executeCommand(
        command: Command,
        program: String?
    ): CommandResult =
        try {
            val result = execute(command)
            if (result.exitCode == CodeCommandNotFound) {
                val errorMessage = result.errorMessage.ifEmpty { "Command not found." }
                throw CommandExecutorException(errorMessage)
            } else if (result.exitCode == CodeExitWithSomeError) {
                val errorMessage = result.errorMessage.ifEmpty { "Exited with some error." }
                throw CommandExecutorException(errorMessage)
            }

            result
        } catch (ioException: IOException) {
            if (ioException.message?.contains("error=2") == true) {
                throw CommandExecutorException("Cannot run program \"${program.orEmpty()}\": No such file or directory")
            }
            throw ioException
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
    private suspend fun Process.gobbleStream() =
        coroutineScope {
            val output = async { inputStream.bufferedReader().use { it.readText() } }
            val error = async { errorStream.bufferedReader().use { it.readText() } }
            val exitCode = waitFor()
            CommandResult(exitCode, output.await(), error.await())
        }

    class CommandExecutorException(
        message: String
    ) : Throwable(message)

    companion object {
        private const val CodeExitWithSomeError = 1
        private const val CodeCommandNotFound = 127
    }
}
