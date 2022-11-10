package com.github.octaone.alcubierre.reducer

import android.net.Uri
import com.github.octaone.alcubierre.action.DeeplinkForward
import com.github.octaone.alcubierre.action.Forward
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.deeplink.DeeplinkResolver
import com.github.octaone.alcubierre.reduce.NavReducer
import com.github.octaone.alcubierre.state.RootNavState

class DeeplinkReducer(
    private val origin: NavReducer<RootNavState>
) : NavReducer<RootNavState> {

    private val resolver = DeeplinkResolver()

    override fun reduce(state: RootNavState, action: NavAction): RootNavState =
        when (action) {
            is DeeplinkForward -> {
                origin.reduce(
                    state,
                    Forward(
                        listOf(resolver.resolve(Uri.parse(action.deeplink)))
                    )
                )
            }
            else -> {
                origin.reduce(state, action)
            }
        }
}