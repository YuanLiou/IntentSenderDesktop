package shellcommands

import utils.OsPlatform
import utils.SystemChecker

class AdbPathHelper(
    private val systemChecker: SystemChecker
) {
    fun lookUpAdbPath(inputPath: String = ""): String {
        val adbPath =
            inputPath.ifBlank {
                when (systemChecker.checkSystem()) {
                    OsPlatform.MAC -> DefaultAdbPathMacOS
                    OsPlatform.WINDOWS -> DefaultAdbPathWindows
                    OsPlatform.LINUX -> DefaultAdbPathLinux
                    OsPlatform.OTHER -> ""
                }
            }
        return adbPath
    }

    companion object {
        private const val DefaultAdbPathMacOS = "~/Library/Android/sdk/platform-tools/adb"
        private const val DefaultAdbPathLinux = "~/Android/Sdk/platform-tools/adb"
        private const val DefaultAdbPathWindows = "%LOCALAPPDATA%\\Android\\sdk\\platform-tools\\adb.exe"
    }
}
