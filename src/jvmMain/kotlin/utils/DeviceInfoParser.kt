package utils

class DeviceInfoParser(
    private val systemChecker: SystemChecker
) {
    /* sample
List of devices attached
22141FDEE000TW	device
emulator-5554	device
     */

    fun parse(input: String): Map<String, List<String>> {
        val delimeter = lookUpDelimiter()
        val stringRemovedFirstLine = input.substringAfter(delimeter)
        if (stringRemovedFirstLine.isBlank()) {
            return emptyMap()
        }

        val lines = stringRemovedFirstLine.split(delimeter).filter { it.isNotEmpty() }
        val result = mutableMapOf<String, MutableList<String>>()
        for (line in lines) {
            val parts = line.split("\t")
            if (parts.size == 2) {
                val key = parts[1]
                val value = parts[0]
                if (result.containsKey(key)) {
                    result[key]?.add(value)
                } else {
                    result[key] = mutableListOf(value)
                }
            }
        }
        return result
    }

    private fun lookUpDelimiter(): String {
        return when (systemChecker.checkSystem()) {
            OsPlatform.MAC, OsPlatform.LINUX -> "\n"
            OsPlatform.WINDOWS -> "\r\n"
            OsPlatform.OTHER -> "\n"
        }
    }
}
