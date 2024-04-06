package com.github.octaone.alcubierre.reducer

import android.net.Uri
import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.action.DeeplinkForward
import com.github.octaone.alcubierre.action.Forward
import com.github.octaone.alcubierre.action.ShowDialog
import com.github.octaone.alcubierre.condition.ConditionalTarget
import com.github.octaone.alcubierre.condition.action.ResolveCondition
import com.github.octaone.alcubierre.deeplink.DeeplinkResolver
import com.github.octaone.alcubierre.reduce.LinkedNavReducer
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.state.AnyRootNavState

class DeeplinkReducer(
    private val onResolveFailed: (Uri) -> Unit
) : LinkedNavReducer<AnyRootNavState>() {

    private val resolver = DeeplinkResolver()

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState =
        when (action) {
            is DeeplinkForward -> {
                val resolvedAction = resolver.resolve(action.deeplink)
                    .map { resolved ->
                        when (resolved) {
                            is Screen -> Forward(listOf(resolved))
                            is Dialog -> ShowDialog(resolved)
                            is ConditionalTarget -> ResolveCondition(resolved)
                            else -> null
                        }
                    }
                    .getOrNull()

                if (resolvedAction != null) {
                    head.reduce(state, resolvedAction)
                } else {
                    onResolveFailed(action.deeplink)
                    state
                }
            }
            else -> {
                next.reduce(state, action)
            }
        }
}