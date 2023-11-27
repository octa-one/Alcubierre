package com.github.octaone.alcubierre.render.internal

import androidx.compose.runtime.SnapshotMutationPolicy
import com.github.octaone.alcubierre.screen.ComposeScreen

object ScreenSnapshotMutationPolicy : SnapshotMutationPolicy<ComposeScreen?> {

    override fun equivalent(a: ComposeScreen?, b: ComposeScreen?): Boolean =
        if (a != null && b != null) {
            a.screenId == b.screenId
        } else {
            a != null || b != null
        }
}
