package com.github.octaone.alcubierre.render

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.github.octaone.alcubierre.ComposeNavDriveOwner
import com.github.octaone.alcubierre.lifecycle.LifecycleHandler
import com.github.octaone.alcubierre.render.internal.ScreenTransitionScope
import com.github.octaone.alcubierre.screen.ComposeScreen
import com.github.octaone.alcubierre.state.ComposeRootNavState

@Composable
fun AlcubierreRender(
    navDriveOwner: ComposeNavDriveOwner,
    addTransition: ScreenTransitionScope.() -> ContentTransform = NONE_TRANSITION,
    removeTransition: ScreenTransitionScope.() -> ContentTransform = NONE_TRANSITION
) {

    val stateHolder: SaveableStateHolder = rememberSaveableStateHolder()

    @SuppressLint("StateFlowValueCalledInComposition")
    val composeState by produceState(initialValue = navDriveOwner.stateFlow.value) {
        var previousState: ComposeRootNavState? = null
        navDriveOwner.stateFlow.collect { currentState ->
            disposeUnusedScreens(previousState, currentState, stateHolder)
            previousState = currentState
            value = currentState
        }
    }

    CurrentScreen(
        composeState = composeState,
        stateHolder = stateHolder,
        addTransition = addTransition,
        removeTransition = removeTransition
    )

    CurrentDialog(
        composeState = composeState,
        onDismissRequest = navDriveOwner::requestDismissDialog
    )
}

@Composable
private fun CurrentScreen(
    composeState: ComposeRootNavState,
    stateHolder: SaveableStateHolder,
    addTransition: ScreenTransitionScope.() -> ContentTransform,
    removeTransition: ScreenTransitionScope.() -> ContentTransform,
) {
    val parentLifecycle = LocalLifecycleOwner.current

    if (addTransition === NONE_TRANSITION && removeTransition === NONE_TRANSITION) {
        composeState.currentScreen?.let { targetScreen ->
            ContentWithProviders(targetScreen, parentLifecycle, stateHolder)
        }
    } else {
        AnimatedContent(
            targetState = composeState,
            contentKey = { state -> state.currentScreen?.screenId },
            transitionSpec = {
                val initialStateScreen = initialState.currentScreen
                val targetStateScreen = targetState.currentScreen
                val transitionScope = ScreenTransitionScope(initialStateScreen, targetStateScreen)

                val initialStateScreenId = initialStateScreen?.screenId
                if (initialStateScreenId == null) {
                    addTransition.invoke(transitionScope)
                } else {
                    var isScreenRemoved = true
                    stacksLoop@ for (stackState in targetState.stackStates.values) {
                        val chain = stackState.chain
                        for (i in chain.indices) {
                            if (chain[i].screenId == initialStateScreenId) {
                                isScreenRemoved = false
                                break@stacksLoop
                            }
                        }
                    }
                    if (isScreenRemoved) {
                        removeTransition.invoke(transitionScope)
                    } else {
                        addTransition.invoke(transitionScope)
                    }
                }
            },
            label = "NavScreenTransition"
        ) { targetState ->
            targetState.currentScreen?.let { targetScreen ->
                ContentWithProviders(targetScreen, parentLifecycle, stateHolder)
            }
        }
    }
}

@Composable
private fun CurrentDialog(
    composeState: ComposeRootNavState,
    onDismissRequest: () -> Unit
) {
    composeState.currentDialog?.let { dialog ->
        key(dialog.dialogId) {
            dialog.Content(onDismissRequest)
        }
    }
}

@Composable
private fun ContentWithProviders(
    targetScreen: ComposeScreen,
    parentLifecycle: LifecycleOwner,
    stateHolder: SaveableStateHolder
) {
    CompositionLocalProvider(
        LocalLifecycleOwner provides targetScreen.lifecycleOwner,
        LocalViewModelStoreOwner provides targetScreen.lifecycleOwner,
        LocalSavedStateRegistryOwner provides targetScreen.lifecycleOwner,
    ) {
        stateHolder.SaveableStateProvider(key = targetScreen.screenId) {
            targetScreen.lifecycleOwner.LifecycleHandler(parentLifecycle.lifecycle)
            targetScreen.Content()
        }
    }
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

private val NONE_TRANSITION: ScreenTransitionScope.() -> ContentTransform = {
    ContentTransform(EnterTransition.None, ExitTransition.None, 0f, null)
}
