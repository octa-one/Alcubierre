package com.github.octaone.alcubierre.reducer

import android.net.Uri
import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.action.DeeplinkForward
import com.github.octaone.alcubierre.action.Forward
import com.github.octaone.alcubierre.action.ShowDialog
import com.github.octaone.alcubierre.deeplink.DeeplinkResolver
import com.github.octaone.alcubierre.reduce.NavReducer
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.state.AnyRootNavState

class DeeplinkReducer(
    private val origin: NavReducer<AnyRootNavState>
) : NavReducer<AnyRootNavState> {

    private val resolver = DeeplinkResolver()

    override fun reduce(state: AnyRootNavState, action: AnyNavAction): AnyRootNavState =
        when (action) {
            is DeeplinkForward -> {
                val command = when (val resolved = resolver.resolve(Uri.parse(action.deeplink))) {
                    is Screen -> Forward(listOf(resolved))
                    is Dialog -> ShowDialog(resolved)
                    else -> throw IllegalArgumentException("Unknown type resolved: ${resolved::class}")
                }
                origin.reduce(state, command)
            }
            else -> {
                origin.reduce(state, action)
            }
        }
}