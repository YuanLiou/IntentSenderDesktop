package intentpusher

import container.SimpleResult
import container.TaskResult
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import utils.StreamGobbler

class ShellCommandExecutor {

    fun sendDeeplink(
        inputPath: String = DEFAULT_ADB_PATH,
        inputPackageName: String,
        inputContent: String
    ): SimpleResult<String> {
        if (inputContent.isEmpty()) {
            return TaskResult.Failed(CommandExecutorException("package name or content data is empty."))
        }

        val adbPath = if (inputPath.isBlank()) {
            DEFAULT_ADB_PATH
        } else {
            inputPath
        }

        val osName = System.getProperty("os.name").lowercase()
        if (!osName.startsWith("mac")) {
            return TaskResult.Failed(CommandExecutorException("Support Mac only"))
        }

        try {
            executeCommand(adbPath, inputPackageName, inputContent)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return TaskResult.Failed(ioException)
        }

        return TaskResult.Success("success")
    }

    @Throws(IOException::class)
    private fun executeCommand(
        adbPath: String,
        packageName: String,
        content: String
    ) {
        val builder = buildAdbProcess(adbPath, packageName, content)
        println("command: ${builder.command().joinToString(" ")}")
        
        val process = builder.start()
        val streamGobbler = StreamGobbler(process.inputStream) {
            println(it)
        }
        val future = Executors.newSingleThreadExecutor().submit(streamGobbler)
        val exitCode = process.waitFor()
        assert(exitCode == 0)
        future.get(10, TimeUnit.SECONDS)
    }

    private fun buildAdbProcess(
        adbPath: String,
        packageName: String,
        content: String
    ): ProcessBuilder {
        // sample: adb shell am start -a android.intent.action.VIEW -d "your-link" com.myapp
        val processBuilder = ProcessBuilder()
        processBuilder.command(
            "sh",
            "-c",
            adbPath,
            "shell",
            "am",
            "start",
            "-a",
            "android.intent.action.VIEW",
            "-d",
            "\"$content\"",
            packageName
        )
        return processBuilder
    }

    class CommandExecutorException(message: String) : Throwable(message)

    companion object {
        private const val DEFAULT_ADB_PATH = "~/Library/Android/sdk/platform-tools/adb"
    }
}