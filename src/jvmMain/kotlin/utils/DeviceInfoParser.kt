package utils

class DeviceInfoParser {
    /* sample
List of devices attached
22141FDEE000TW	device
emulator-5554	device
     */

    fun parse(input: String): Map<String, List<String>> {
        val stringRemovedFirstLine = input.substringAfter("\n")
        if (stringRemovedFirstLine.isBlank()) {
            return emptyMap()
        }

        val lines = stringRemovedFirstLine.split("\n").filter { it.isNotEmpty() }
        val result = mutableMapOf<String, MutableList<String>>()
        for (line in lines) {
            val parts = line.split("\t")
            if (parts.size == 2) {
                val key = parts.get(1)
                val value = parts.get(0)
                if (result.containsKey(key)) {
                    result[key]?.add(value)
                } else {
                    result[key] = mutableListOf(value)
                }
            }
        }
        return result
    }
}