package usecase

import shellcommands.AdbCommandExecutor
import shellcommands.CommandBuilder
import utils.DeviceInfoParser

class GetDevices(
    private val adbCommandExecutor: AdbCommandExecutor,
    private val commandBuilder: CommandBuilder,
    private val deviceInfoParser: DeviceInfoParser
) {
    suspend operator fun invoke(
        adbPath: String
    ): Result<List<String>> {
        val command = commandBuilder.buildDevicesCommand(adbPath)
        return runCatching {
            val commandResult = adbCommandExecutor.executeCommand(command, adbPath)
            val deviceInfo = deviceInfoParser.parse(commandResult.output)
            if (deviceInfo.containsKey("device")) {
                return@runCatching deviceInfo.get("device").orEmpty()
            }
            emptyList()
        }
    }
}
