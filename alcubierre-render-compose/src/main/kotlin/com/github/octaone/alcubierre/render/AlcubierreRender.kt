package com.github.octaone.alcubierre.render

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.github.octaone.alcubierre.ComposeNavDriveOwner
import com.github.octaone.alcubierre.lifecycle.LifecycleHandler
import com.github.octaone.alcubierre.render.internal.DialogRootNavStateProjection
import com.github.octaone.alcubierre.render.internal.ImmutableSaveableStateHolder
import com.github.octaone.alcubierre.render.internal.ScreenRootNavStateProjection
import com.github.octaone.alcubierre.render.internal.ScreenTransitionScope
import com.github.octaone.alcubierre.screen.ComposeScreen
import com.github.octaone.alcubierre.screen.Content
import com.github.octaone.alcubierre.screen.getContent
import com.github.octaone.alcubierre.state.ComposeRootNavState

@Composable
@Suppress("NonSkippableComposable")
fun AlcubierreRender(
    navDriveOwner: ComposeNavDriveOwner,
    addTransition: ScreenTransitionScope.() -> ContentTransform = NONE_TRANSITION,
    removeTransition: ScreenTransitionScope.() -> ContentTransform = addTransition
) {

    val stateHolder = ImmutableSaveableStateHolder(rememberSaveableStateHolder())

    @SuppressLint("StateFlowValueCalledInComposition")
    val currentState by produceState(initialValue = navDriveOwner.stateFlow.value) {
        var previousState: ComposeRootNavState? = null
        navDriveOwner.stateFlow.collect { currentState ->
            disposeDialog(previousState, currentState, stateHolder)
            disposeUnusedScreens(previousState, currentState, stateHolder)
            previousState = currentState
            value = currentState
        }
    }

    if (addTransition === NONE_TRANSITION && removeTransition === NONE_TRANSITION) {
        ImmediateCurrentScreen(
            currentStateProjection = ScreenRootNavStateProjection(currentState),
            stateHolder = stateHolder,
        )
    } else {
        AnimatedCurrentScreen(
            currentStateProjection = ScreenRootNavStateProjection(currentState),
            stateHolder = stateHolder,
            addTransition = addTransition,
            removeTransition = removeTransition
        )
    }

    CurrentDialog(
        currentStateProjection = DialogRootNavStateProjection(currentState),
        stateHolder = stateHolder,
        onDismissRequest = navDriveOwner::requestDismissDialog
    )
}

@Composable
private fun ImmediateCurrentScreen(
    currentStateProjection: ScreenRootNavStateProjection,
    stateHolder: ImmutableSaveableStateHolder
) {
    CurrentScreen(
        currentStateProjection = currentStateProjection,
        stateHolder = stateHolder
    )

    // To have the order of lifecycle events as in the case of an animated transition.
    val currentScreen = currentStateProjection.state.currentScreen
    val previousScreenRef = remember { Ref<ComposeScreen>() }
    LaunchedEffect(currentScreen) {
        previousScreenRef.value?.lifecycleManager?.onExitTransitionFinished()
        previousScreenRef.value = currentScreen
        currentScreen?.lifecycleManager?.onEnterTransitionFinished()
    }
}

@Composable
private fun AnimatedCurrentScreen(
    currentStateProjection: ScreenRootNavStateProjection,
    stateHolder: ImmutableSaveableStateHolder,
    addTransition: ScreenTransitionScope.() -> ContentTransform,
    removeTransition: ScreenTransitionScope.() -> ContentTransform,
) {
    val transition = updateTransition(currentStateProjection.state, label = "Alcubierre")
    transition.AnimatedContent(
        contentKey = { state ->
            state.currentScreen?.screenId
        },
        transitionSpec = {
            val transitionScope = ScreenTransitionScope(initialState.currentScreen, targetState.currentScreen)
            // No need to check if the previous screen is removed if the transitions are the same.
            if (addTransition === removeTransition && isPreviousScreenRemoved(initialState, targetState)) {
                removeTransition.invoke(transitionScope)
            } else {
                addTransition.invoke(transitionScope)
            }
        }
    ) { targetState ->
        CurrentScreen(ScreenRootNavStateProjection(targetState), stateHolder)
    }

    val previousScreenRef = remember { Ref<ComposeScreen>() }
    LaunchedEffect(transition.currentState.currentScreen, transition.targetState.currentScreen) {
        if (transition.currentState.currentScreen == transition.targetState.currentScreen) {
            previousScreenRef.value?.lifecycleManager?.onExitTransitionFinished()
            previousScreenRef.value = null
            transition.currentState.currentScreen?.lifecycleManager?.onEnterTransitionFinished()
        } else {
            previousScreenRef.value = transition.currentState.currentScreen
        }
    }
    // When you leave the screen, the current transition is completed.
    DisposableEffect(Unit) {
        onDispose {
            previousScreenRef.value?.lifecycleManager?.onExitTransitionFinished()
        }
    }
}

@Composable
private fun CurrentScreen(
    currentStateProjection: ScreenRootNavStateProjection,
    stateHolder: ImmutableSaveableStateHolder
) {
    currentStateProjection.state.currentScreen?.let { screen ->
        val content = screen.getContent(LocalContext.current.classLoader)
        val parentLifecycle = LocalLifecycleOwner.current.lifecycle
        CompositionLocalProvider(
            LocalLifecycleOwner provides screen.lifecycleManager,
            LocalViewModelStoreOwner provides screen.lifecycleManager,
            LocalSavedStateRegistryOwner provides screen.lifecycleManager
        ) {
            stateHolder.SaveableStateProvider(screen.screenId) {
                screen.lifecycleManager.LifecycleHandler(parentLifecycle)
                content.Content(screen)
            }
        }
    }
}

@Composable
private fun CurrentDialog(
    currentStateProjection: DialogRootNavStateProjection,
    stateHolder: ImmutableSaveableStateHolder,
    onDismissRequest: () -> Unit,
) {
    currentStateProjection.state.currentDialog?.let { dialog ->
        key(dialog.dialogId) {

            val content = dialog.getContent(LocalContext.current.classLoader)
            val parentLifecycle = LocalLifecycleOwner.current.lifecycle
            CompositionLocalProvider(
                LocalLifecycleOwner provides dialog.lifecycleManager,
                LocalViewModelStoreOwner provides dialog.lifecycleManager,
                LocalSavedStateRegistryOwner provides dialog.lifecycleManager
            ) {
                stateHolder.SaveableStateProvider(dialog.dialogId) {
                    dialog.lifecycleManager.LifecycleHandler(parentLifecycle)
                    content.Content(dialog, onDismissRequest)
                }
            }
        }
    }
}

private fun isPreviousScreenRemoved(previousState: ComposeRootNavState, targetState: ComposeRootNavState): Boolean {
    val previousScreen = previousState.currentScreen ?: return false
    val previousScreenId = previousScreen.screenId

    var isScreenRemoved = true
    stacksLoop@ for (stackState in targetState.stackStates.values) {
        val chain = stackState.stack
        for (i in chain.indices) {
            if (chain[i].screenId == previousScreenId) {
                isScreenRemoved = false
                break@stacksLoop
            }
        }
    }

    return isScreenRemoved
}

private suspend fun disposeDialog(
    previous: ComposeRootNavState?,
    current: ComposeRootNavState,
    stateHolder: SaveableStateHolder
) {
    val previousDialog = previous?.currentDialog ?: return
    val currentDialog = current.currentDialog
    if (previousDialog.dialogId == currentDialog?.dialogId) return

    previousDialog.hide()
    previousDialog.lifecycleManager.onRemoved()
    stateHolder.removeState(previousDialog.dialogId)
}

private fun disposeUnusedScreens(
    previous: ComposeRootNavState?,
    current: ComposeRootNavState,
    stateHolder: SaveableStateHolder
) {
    if (previous == null) return

    val previousScreen = previous.currentScreen
    if (previousScreen != null && previousScreen.screenId != current.currentScreen?.screenId) {
        // Actually the screen can be removed (not stacked), in that case onRemoved will be called next after onStacked.
        previousScreen.lifecycleManager.onStacked()
    }

    previous.stackStates.forEach { (id, previousStackState) ->
        val currentStackState = current.stackStates[id]
        var firstMismatchIndex = 0
        if (currentStackState != null) {
            // The idea is the same as with fragments.
            // All screens are closed starting from the first mismatch from the root.
            val minIndex = minOf(previousStackState.stack.lastIndex, currentStackState.stack.lastIndex)
            firstMismatchIndex = currentStackState.stack.size
            for (i in 0..minIndex) {
                if (previousStackState.stack[i].screenId != currentStackState.stack[i].screenId) {
                    firstMismatchIndex = i
                    break
                }
            }
        }

        for (i in firstMismatchIndex..previousStackState.stack.lastIndex) {
            val screen = previousStackState.stack[i]
            screen.lifecycleManager.onRemoved()
            stateHolder.removeState(screen.screenId)
        }
    }
}

private val NONE_TRANSITION: ScreenTransitionScope.() -> ContentTransform = {
    ContentTransform(EnterTransition.None, ExitTransition.None, 0f, null)
}
