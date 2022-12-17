package com.github.octaone.alcubierre.lint

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Detector.UastScanner
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Incident
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.kotlin.KotlinUClass

class ScreenHashCodeEqualsDetector : Detector(), UastScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> =
        listOf(UClass::class.java)

    override fun applicableSuperClasses(): List<String> =
        listOf(SCREEN, DIALOG)

    override fun visitClass(context: JavaContext, declaration: UClass) {
        if (declaration is KotlinUClass && declaration.sourcePsi is KtObjectDeclaration) return
        if (declaration.qualifiedName in ALLOWED_CLASSES) return

        val containsEqualHashCode = declaration.methods
            .map { it.name }
            .containsAll(DATA_METHODS)

        if (!containsEqualHashCode) {
            context.report(
                Incident(
                    issue = ISSUE_NON_DATA_SCREEN_CLASS_RULE,
                    scope = declaration,
                    location = context.getNameLocation(declaration),
                    message = "Screen classes should override hashCode and equals."
                )
            )
        }
    }

    companion object {

        val ISSUE_NON_DATA_SCREEN_CLASS_RULE: Issue = Issue.create(
            id = "NonDataScreenClassRule",
            briefDescription = "Screen class without hashCode/equals implementation.",
            explanation = "Screen classes should override hashCode and equals.",
            category = Category.CORRECTNESS,
            priority = 9,
            severity = Severity.ERROR,
            implementation = Implementation(ScreenHashCodeEqualsDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        private const val PACKAGE = "com.github.octaone.alcubierre"
        private const val SCREEN = "$PACKAGE.screen.Screen"
        private const val DIALOG = "$PACKAGE.screen.Dialog"
        private val DATA_METHODS = listOf("equals", "hashCode")

        private val ALLOWED_CLASSES = listOf(
            SCREEN,
            DIALOG,
            "$PACKAGE.screen.FragmentScreen",
            "$PACKAGE.screen.FragmentDialog"
        )
    }
}