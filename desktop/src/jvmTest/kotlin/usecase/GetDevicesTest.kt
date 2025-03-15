package usecase

import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import shellcommands.AdbCommandExecutor
import shellcommands.Command
import shellcommands.CommandBuilder
import shellcommands.CommandResult
import utils.DeviceInfoParser

class GetDevicesTest {
    @MockK
    lateinit var adbCommandExecutor: AdbCommandExecutor

    @MockK
    lateinit var commandBuilder: CommandBuilder

    @MockK
    lateinit var deviceInfoParser: DeviceInfoParser

    lateinit var getDevices: GetDevices

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getDevices =
            GetDevices(
                adbCommandExecutor,
                commandBuilder,
                deviceInfoParser
            )
    }

    @Test
    fun testIfThereIsNoDeviceConnected() =
        runTest {
            // expected: an emptyList
            // Given
            val emptyCommand = Command(emptyList())
            every { commandBuilder.buildDevicesCommand(any()) } returns emptyCommand
            coEvery { adbCommandExecutor.executeCommand(emptyCommand, "") } returns CommandResult(0, "", "")
            every { deviceInfoParser.parse(any()) } returns emptyMap() // <== main testing operation

            // When
            val result = getDevices("")

            // Then
            Truth.assertThat(result.isSuccess).isTrue()
            Truth.assertThat(result.getOrNull()?.size).isEqualTo(0)
        }

    @Test
    fun testIfThereIsNoActiveDeviceConnected() =
        runTest {
            // expected: an emptyList
            // Given
            val emptyCommand = Command(emptyList())
            every { commandBuilder.buildDevicesCommand(any()) } returns emptyCommand
            coEvery { adbCommandExecutor.executeCommand(emptyCommand, "") } returns CommandResult(0, "", "")
            // main testing operation
            every { deviceInfoParser.parse(any()) } returns
                mapOf(
                    "recovery" to listOf("Test Machine 01", "Test Machine 02"),
                    "fastboot" to listOf("Test Machine 03")
                )

            // When
            val result = getDevices("")

            // Then
            Truth.assertThat(result.isSuccess).isTrue()
            Truth.assertThat(result.getOrNull()?.size).isEqualTo(0)
        }
}
