package com.github.octaone.alcubierre.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import com.google.auto.service.AutoService

@AutoService(value = [IssueRegistry::class])
class AlcubierreIssueRegistry : IssueRegistry() {

    override val issues: List<Issue>
        get() = listOf(ScreenHashCodeEqualsDetector.ISSUE_NON_DATA_SCREEN_CLASS_RULE)

    override val api: Int
        get() = CURRENT_API

    /**
     * works with Studio 4.0 or later; see
     * [com.android.tools.lint.detector.api.describeApi]
     */
    override val minApi: Int
        get() = 7

    override val vendor = Vendor(
        vendorName = "octa-one/Alcubierre",
        identifier = "com.github.octaone.alcubierre",
        feedbackUrl = "https://github.com/octa-one/Alcubierre/issues",
    )
}
