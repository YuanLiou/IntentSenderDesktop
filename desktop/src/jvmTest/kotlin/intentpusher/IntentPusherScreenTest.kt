package intentpusher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IntentPusherScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @RelaxedMockK
    lateinit var intentPusherViewModel: IntentPusherViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `show proper adb path`() {
        // Expected: show the same adbPath as given value
        // Given
        val adbPath = "~/Android/Sdk/platform-tools/adb"
        every { intentPusherViewModel.inputPath } returns adbPath
        every { intentPusherViewModel.viewStates } returns MutableStateFlow<IntentPusherViewState>(IntentPusherViewState.WaitForUserInput)

        // When
        composeTestRule.setContent {
            IntentPusherScreen(viewModel = intentPusherViewModel)
        }

        // Then
        composeTestRule.onNodeWithText(adbPath).assertIsDisplayed()
    }
}
