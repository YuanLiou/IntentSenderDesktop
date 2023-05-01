package utils

import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

class DeviceInfoParserTest {

    lateinit var deviceInfoParser: DeviceInfoParser

    @Before
    fun setup() {
        deviceInfoParser = DeviceInfoParser()
    }

    @Test
    fun parseWithTwoConnectedDevices() {
        /*  Sample Input
            List of devices attached
            22141FDEE000TW	device
            emulator-5554	device
         */

        // Expected: Map<deviceType: String, deviceNames: List<String>>
        // e.g. mapOf("device" to listOf("22141FDEE000TW", "emulator-5554")

        // Given
        val sampleInput = "List of devices attached\n22141FDEE000TW\tdevice\nemulator-5554\tdevice\n\n"

        // When
        val resultMap = deviceInfoParser.parse(sampleInput)

        // Then
        Truth.assertThat(resultMap.size).isGreaterThan(0)
        Truth.assertThat(resultMap.containsKey("device")).isTrue()
        Truth.assertThat(resultMap["device"]?.first()).isEqualTo("22141FDEE000TW")
    }

    @Test
    fun parseWithNoConnectedDevices() {
        /*  Sample Input
            List of devices attached
         */

        // Expected: Map<deviceType: String, deviceNames: List<String>>
        // e.g. emptyMap()

        // Given
        val sampleInput = "List of devices attached\n\n"

        // When
        val resultMap = deviceInfoParser.parse(sampleInput)

        // Then
        Truth.assertThat(resultMap.size).isEqualTo(0)
        Truth.assertThat(resultMap.containsKey("device")).isFalse()
    }
}
