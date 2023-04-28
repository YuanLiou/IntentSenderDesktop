package shellcommands

import utils.OsPlatform
import utils.SystemChecker

class AdbPathHelper(
    private val systemChecker: SystemChecker
) {

    fun lookUpAdbPath(inputPath: String = ""): String {
        val adbPath = inputPath.ifBlank {
            when (systemChecker.checkSystem()) {
                OsPlatform.MAC, OsPlatform.LINUX -> DEFAULT_ADB_PATH_MACOS
                OsPlatform.WINDOWS -> DEFAULT_ADB_PATH_WINDOWS
                OsPlatform.OTHER -> ""
            }
        }
        return adbPath
    }

    companion object {
        private const val DEFAULT_ADB_PATH_MACOS = "~/Library/Android/sdk/platform-tools/adb"
        private const val DEFAULT_ADB_PATH_WINDOWS = "%LOCALAPPDATA%\\Android\\sdk\\platform-tools\\adb.exe"
    }
}