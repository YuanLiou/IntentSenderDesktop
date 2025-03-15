package usecase

import shellcommands.AdbCommandExecutor
import shellcommands.CommandBuilder
import shellcommands.CommandResult

class SendDeepLink(
    private val adbCommandExecutor: AdbCommandExecutor,
    private val commandBuilder: CommandBuilder
) {
    suspend operator fun invoke(
        adbPath: String,
        deviceName: String?,
        inputPackageName: String,
        inputContent: String
    ): Result<CommandResult> {
        if (inputContent.isEmpty()) {
            return Result.failure(IllegalArgumentException("content is empty"))
        }

        val command =
            commandBuilder.buildDeepLinkCommand(
                adbPath,
                deviceName,
                inputPackageName,
                inputContent
            )
        return runCatching {
            adbCommandExecutor.executeCommand(command, adbPath)
        }
    }
}
