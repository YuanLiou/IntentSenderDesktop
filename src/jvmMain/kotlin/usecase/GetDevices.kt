package usecase

import shellcommands.AdbCommandExecutor
import shellcommands.CommandBuilder
import shellcommands.CommandResult
import utils.DeviceInfoParser

class GetDevices(
    private val adbCommandExecutor: AdbCommandExecutor,
    private val commandBuilder: CommandBuilder,
    private val deviceInfoParser: DeviceInfoParser
) {
    suspend operator fun invoke(
        adbPath: String
    ): Result<Map<String, List<String>>> {
        val command = commandBuilder.buildDevicesCommand(adbPath)
        return runCatching {
            val commandResult = adbCommandExecutor.executeCommand(command, adbPath)
            deviceInfoParser.parse(commandResult.output)
        }
    }
}