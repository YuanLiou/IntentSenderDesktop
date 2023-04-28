package usecase

import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import shellcommands.AdbCommandExecutor
import shellcommands.CommandBuilder

class SendDeepLinkTest {

    @MockK
    lateinit var adbCommandExecutor: AdbCommandExecutor
    @MockK
    lateinit var commandBuilder: CommandBuilder

    lateinit var sendDeepLink: SendDeepLink

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sendDeepLink = SendDeepLink(adbCommandExecutor, commandBuilder)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSendEmptyContent() = runTest {
        // Given
        val testPath = "~/testPath/adb"
        val testPackageName = "com.sample"
        val testContent = ""

        // When
        val result = sendDeepLink(testPath, testPackageName, testContent)

        // Then
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
    }
}