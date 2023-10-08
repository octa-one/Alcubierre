package com.github.octaone.alcubierre.render

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.github.octaone.alcubierre.ComposeNavDriveOwner
import com.github.octaone.alcubierre.lifecycle.LifecycleHandler
import com.github.octaone.alcubierre.state.ComposeRootNavState

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AlcubierreRender(navDriveOwner: ComposeNavDriveOwner) {

    val stateHolder: SaveableStateHolder = rememberSaveableStateHolder()

    val composeState by produceState(initialValue = navDriveOwner.stateFlow.value) {
        var previousState: ComposeRootNavState? = null
        navDriveOwner.stateFlow.collect { currentState ->
            disposeUnusedScreens(previousState, currentState, stateHolder)
            previousState = currentState
            value = currentState
        }
    }

    val currentScreen by remember {
        derivedStateOf { composeState.currentStackState.chain.lastOrNull() }
    }
    currentScreen?.let { screen ->
        val parentLifecycle = LocalLifecycleOwner.current
        CompositionLocalProvider(
            LocalLifecycleOwner provides screen.lifecycleOwner,
            LocalViewModelStoreOwner provides screen.lifecycleOwner,
            LocalSavedStateRegistryOwner provides screen.lifecycleOwner,
        ) {
            stateHolder.SaveableStateProvider(key = screen.screenId) {
                screen.lifecycleOwner.LifecycleHandler(parentLifecycle.lifecycle)
                screen.Content()
            }
        }
    }

    // TODO Dialogs
}

private fun disposeUnusedScreens(
    previous: ComposeRootNavState?,
    current: ComposeRootNavState,
    stateHolder: SaveableStateHolder
) {
    if (previous == null) return

    val previousCurrentScreen = previous.currentScreen
    if (previousCurrentScreen != null && previousCurrentScreen != current.currentScreen) {
        previousCurrentScreen.lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    previous.stackStates.forEach { (id, previousStackState) ->
        val currentStackState = current.stackStates[id]
        if (currentStackState != null) {

            // The idea is the same as with fragments.
            // All screens are closed starting from the first mismatch from the root.
            val minIndex = minOf(previousStackState.chain.lastIndex, currentStackState.chain.lastIndex)
            var firstMismatchIndex = currentStackState.chain.size
            for (i in 0..minIndex) {
                if (previousStackState.chain[i].screenId != currentStackState.chain[i].screenId) {
                    firstMismatchIndex = i
                    break
                }
            }

            for (i in firstMismatchIndex..previousStackState.chain.lastIndex) {
                val screen = previousStackState.chain[i]
                screen.lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                stateHolder.removeState(screen.screenId)
            }
        } else {
            previousStackState.chain.forEach { screen ->
                screen.lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                stateHolder.removeState(screen.screenId)
            }
        }
    }
}
