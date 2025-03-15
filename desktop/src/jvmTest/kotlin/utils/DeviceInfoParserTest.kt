package utils

import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

class DeviceInfoParserTest {
    @MockK
    lateinit var systemChecker: SystemChecker

    private lateinit var deviceInfoParser: DeviceInfoParser

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        deviceInfoParser = DeviceInfoParser(systemChecker)
    }

    @Test
    fun parseWithTwoConnectedDevicesOnMac() {
        /*  Sample Input
            List of devices attached
            22141FDEE000TW	device
            emulator-5554	device
         */

        // Expected: Map<deviceType: String, deviceNames: List<String>>
        // e.g. mapOf("device" to listOf("22141FDEE000TW", "emulator-5554")

        // Given
        val sampleInput = "List of devices attached\n22141FDEE000TW\tdevice\nemulator-5554\tdevice\n\n"
        every { systemChecker.checkSystem() } returns OsPlatform.MAC

        // When
        val resultMap = deviceInfoParser.parse(sampleInput)

        // Then
        Truth.assertThat(resultMap.size).isGreaterThan(0)
        Truth.assertThat(resultMap.containsKey("device")).isTrue()
        Truth.assertThat(resultMap["device"]?.first()).isEqualTo("22141FDEE000TW")
    }

    @Test
    fun parseWithNoConnectedDevicesOnMac() {
        /*  Sample Input
            List of devices attached
         */

        // Expected: Map<deviceType: String, deviceNames: List<String>>
        // e.g. emptyMap()

        // Given
        val sampleInput = "List of devices attached\n\n"
        every { systemChecker.checkSystem() } returns OsPlatform.MAC

        // When
        val resultMap = deviceInfoParser.parse(sampleInput)

        // Then
        Truth.assertThat(resultMap.size).isEqualTo(0)
        Truth.assertThat(resultMap.containsKey("device")).isFalse()
    }

    @Test
    fun parseWithConnectedDevicesOnWindows() {
        /*  Sample Input
            List of devices attached
            emulator-5554	device
         */

        // Expected: Map<deviceType: String, deviceNames: List<String>>
        // e.g. mapOf("device" to listOf("emulator-5554")

        // Given
        val sampleInput = "List of devices attached\r\nemulator-5554\tdevice\r\n\r\n"
        every { systemChecker.checkSystem() } returns OsPlatform.WINDOWS

        // When
        val resultMap = deviceInfoParser.parse(sampleInput)

        // Then
        Truth.assertThat(resultMap.size).isGreaterThan(0)
        Truth.assertThat(resultMap.containsKey("device")).isTrue()
        Truth.assertThat(resultMap["device"]?.first()).isEqualTo("emulator-5554")
    }

    @Test
    fun parseWithNoConnectedDevicesOnWindows() {
        /*  Sample Input
            List of devices attached
         */

        // Expected: Map<deviceType: String, deviceNames: List<String>>
        // e.g. emptyMap()

        // Given
        val sampleInput = "List of devices attached\r\n\r\n"
        every { systemChecker.checkSystem() } returns OsPlatform.WINDOWS

        // When
        val resultMap = deviceInfoParser.parse(sampleInput)

        // Then
        Truth.assertThat(resultMap.size).isEqualTo(0)
        Truth.assertThat(resultMap.containsKey("device")).isFalse()
    }
}
