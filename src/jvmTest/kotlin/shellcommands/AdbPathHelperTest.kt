package shellcommands

import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK

import org.junit.Before
import org.junit.Test
import utils.OsPlatform
import utils.SystemChecker

class AdbPathHelperTest {

    @MockK
    lateinit var systemChecker: SystemChecker

    lateinit var adbPathHelper: AdbPathHelper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        adbPathHelper = AdbPathHelper(systemChecker)
    }

    @Test
    fun lookUpAdbPathWithEmptyPathAtMac() {
        // Expect: "~/Library/Android/sdk/platform-tools/adb"

        // Given
        every { systemChecker.checkSystem() } returns OsPlatform.MAC

        // When
        val path = adbPathHelper.lookUpAdbPath("")

        // Then
        val expected = "~/Library/Android/sdk/platform-tools/adb"
        Truth.assertThat(path).isEqualTo(expected)
    }

    @Test
    fun lookUpAdbPathWithRandomInput() {
        // Expect: "/usr/share/android-sdk/platform-tools/adb"

        // Given
        every { systemChecker.checkSystem() } returns OsPlatform.MAC
        val myInputPath = "/usr/share/android-sdk/platform-tools/adb"

        // When
        val path = adbPathHelper.lookUpAdbPath(myInputPath)

        // Then
        Truth.assertThat(path).isEqualTo(myInputPath)
    }
}
