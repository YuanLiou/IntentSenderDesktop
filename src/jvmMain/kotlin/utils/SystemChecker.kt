package utils

import org.apache.commons.lang3.SystemUtils

class SystemChecker {
    fun checkSystem(): OsPlatform {
        if (SystemUtils.IS_OS_MAC) {
            return OsPlatform.MAC
        } else if (SystemUtils.IS_OS_WINDOWS) {
            return OsPlatform.WINDOWS
        } else if (SystemUtils.IS_OS_LINUX) {
            return OsPlatform.LINUX
        } else {
            return OsPlatform.OTHER
        }
    }

    fun isMac(): Boolean = checkSystem() == OsPlatform.MAC
    fun isWindows(): Boolean = checkSystem() == OsPlatform.WINDOWS
    fun isLinux(): Boolean = checkSystem() == OsPlatform.LINUX
}