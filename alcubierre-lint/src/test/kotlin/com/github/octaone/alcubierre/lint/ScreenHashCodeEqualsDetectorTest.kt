package com.github.octaone.alcubierre.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class ScreenHashCodeEqualsDetectorTest {

    private val SCREEN_STUB = kotlin("""
        |package com.github.octaone.alcubierre.screen
        |interface Screen {
        |  val screenId: String
        |}""".trimMargin()
    )

    private val FRAGMENT_SCREEN_STUB = kotlin("""
        |package com.github.octaone.alcubierre.screen
        |abstract class FragmentScreen(
        |    val fragmentName: String
        |) : Screen {
        |
        |    override val screenId: String = ""
        |
        |    override fun equals(other: Any?): Boolean = true
        |
        |    override fun hashCode(): Int = 1
        |}""".trimMargin()
    )

    @Test
    fun `test missing data`() {
        lint()
            .files(
                SCREEN_STUB,
                FRAGMENT_SCREEN_STUB,
                kotlin("""
                    |package foo
                    |import com.github.octaone.alcubierre.screen.FragmentScreen
                    |class TestScreen : FragmentScreen("test")
                    |""".trimMargin()
                )
            )
            .issues(ScreenHashCodeEqualsDetector.ISSUE_NON_DATA_SCREEN_CLASS_RULE)
            .run()
            .expect("""
                |src/foo/TestScreen.kt:3: Error: Screen classes should override hashCode and equals. [NonDataScreenClassRule]
                |class TestScreen : FragmentScreen("test")
                |      ~~~~~~~~~~
                |1 errors, 0 warnings""".trimMargin()
            )
    }

    @Test
    fun `test data`() {
        lint()
            .files(
                SCREEN_STUB,
                FRAGMENT_SCREEN_STUB,
                kotlin("""
                    |package foo
                    |import com.github.octaone.alcubierre.screen.FragmentScreen
                    |data class TestScreen(val param: Int) : FragmentScreen("test")
                    |""".trimMargin()
                )
            )
            .issues(ScreenHashCodeEqualsDetector.ISSUE_NON_DATA_SCREEN_CLASS_RULE)
            .run()
            .expectClean()
    }

    @Test
    fun `test custom hashCode and equals`() {
        lint()
            .files(
                SCREEN_STUB,
                FRAGMENT_SCREEN_STUB,
                kotlin(
                    """
                    |package foo
                    |import com.github.octaone.alcubierre.screen.FragmentScreen
                    |class TestScreen : Screen {
                    |    override val screenId: String = ""
                    |    override fun hashCode(): Int = 1
                    |    override fun equals(other: Any?): Boolean = true
                    |}
                    |""".trimMargin()
                )
            )
            .issues(ScreenHashCodeEqualsDetector.ISSUE_NON_DATA_SCREEN_CLASS_RULE)
            .run()
            .expectClean()
    }

    @Test
    fun `test object`() {
        lint()
            .files(
                SCREEN_STUB,
                FRAGMENT_SCREEN_STUB,
                kotlin("""
                    |package foo
                    |import com.github.octaone.alcubierre.screen.FragmentScreen
                    |object TestScreen : FragmentScreen("test")
                    |""".trimMargin()
                )
            )
            .issues(ScreenHashCodeEqualsDetector.ISSUE_NON_DATA_SCREEN_CLASS_RULE)
            .run()
            .expectClean()
    }
}
