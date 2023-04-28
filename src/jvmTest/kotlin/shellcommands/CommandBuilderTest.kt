package shellcommands

import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import utils.OsPlatform
import utils.SystemChecker

class CommandBuilderTest {

    @MockK
    lateinit var systemChecker: SystemChecker

    @MockK
    lateinit var adbPathHelper: AdbPathHelper

    lateinit var commandBuilder: CommandBuilder

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        commandBuilder = CommandBuilder(systemChecker, adbPathHelper)
    }

    @Test
    fun buildDeepLinkCommandOnMac() {
        // expected: sh -c test/path/adb shell 'am start -a android.intent.action.VIEW -d "Hello Test" com.myapp'

        // Given
        every { adbPathHelper.lookUpAdbPath() } returns "test/path/adb"
        every { systemChecker.checkSystem() } returns OsPlatform.MAC

        // When
        val command = commandBuilder.buildDeepLinkCommand(
            adbPath = "",
            packageName = "com.myapp",
            content = "Hello Test"
        )

        // Then
        val expectedCommand = "sh -c test/path/adb shell 'am start -a android.intent.action.VIEW -d \"Hello Test\" com.myapp'"
        Truth.assertThat(command.commands).isNotEmpty()
        Truth.assertThat(command.getFullCommand())
            .isEqualTo(expectedCommand)
    }

    @Test
    fun buildDeepLinkCommandOnWindows() {
        // expected: cmd.exe /c C:\test\mypath\adb.exe shell am start -a android.intent.action.VIEW -d "Hello Test" com.myapp

        // Given
        every { adbPathHelper.lookUpAdbPath() } returns "C:\\test\\mypath\\adb.exe"
        every { systemChecker.checkSystem() } returns OsPlatform.WINDOWS

        // When
        val command = commandBuilder.buildDeepLinkCommand(
            adbPath = "",
            packageName = "com.myapp",
            content = "Hello Test"
        )

        // Then
        val expectedCommand = "cmd.exe /c C:\\test\\mypath\\adb.exe shell am start -a android.intent.action.VIEW -d \"Hello Test\" com.myapp"
        Truth.assertThat(command.commands).isNotEmpty()
        Truth.assertThat(command.getFullCommand())
            .isEqualTo(expectedCommand)
    }
}
