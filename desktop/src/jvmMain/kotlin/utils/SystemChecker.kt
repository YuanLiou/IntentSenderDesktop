package utils

import org.apache.commons.lang3.SystemUtils

class SystemChecker {
    fun checkSystem(): OsPlatform =
        when {
            SystemUtils.IS_OS_MAC -> OsPlatform.MAC
            SystemUtils.IS_OS_WINDOWS -> OsPlatform.WINDOWS
            SystemUtils.IS_OS_LINUX -> OsPlatform.LINUX
            else -> OsPlatform.OTHER
        }

    fun isMac(): Boolean = checkSystem() == OsPlatform.MAC

    fun isWindows(): Boolean = checkSystem() == OsPlatform.WINDOWS

    fun isLinux(): Boolean = checkSystem() == OsPlatform.LINUX
}
