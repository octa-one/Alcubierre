package com.github.octaone.alcubierre.render

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import com.github.octaone.alcubierre.ComposeNavDriveOwner
import com.github.octaone.alcubierre.LocalNavDrive
import com.github.octaone.alcubierre.LocalRenderAnimatedContentScope
import com.github.octaone.alcubierre.lifecycle.LifecycleHandler
import com.github.octaone.alcubierre.lifecycle.ScreenLifecycleOwner
import com.github.octaone.alcubierre.render.internal.DialogRootNavStateProjection
import com.github.octaone.alcubierre.render.internal.ImmutableSaveableStateHolder
import com.github.octaone.alcubierre.render.internal.ScreenRootNavStateProjection
import com.github.octaone.alcubierre.screen.ComposeScreen
import com.github.octaone.alcubierre.screen.HideRequest
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.state.ComposeRootNavState

@Composable
@NonRestartableComposable
internal fun AlcubierreRenderImplWithProvider(
    navDriveOwner: ComposeNavDriveOwner,
    animationType: Int,
    addTransition: (ScreenTransitionScope.() -> ContentTransform)?,
    removeTransition: (ScreenTransitionScope.() -> ContentTransform)?,
    animationSpec: FiniteAnimationSpec<Float>?
) {
    CompositionLocalProvider(LocalNavDrive provides navDriveOwner) {
        AlcubierreRenderImpl(navDriveOwner, animationType, addTransition, removeTransition, animationSpec)
    }
}

/**
 * A few notes on the behavior of the default implementation:
 * * State is produced from [navDriveOwner] StateFlow.
 * * Before the new state is rendered, the old state is disposed.
 *   The dialog closes with a hiding animation if [HideRequest.HideEffect] is registered.
 *   All screens are closed starting from the first mismatch from the root.
 *   This is the same behavior as in fragment rendering.
 * * The lifecycle of screen and dialog is aware of the transition state.
 * * In the default implementation, this means that [Lifecycle.Event.ON_RESUME]
 *   will only be called after the transition is complete. The same is true for the removed screen,
 *   [Lifecycle.Event.ON_DESTROY] will be called after the transition is complete.
 * * Note that [Lifecycle.Event.ON_DESTROY] cannot be observed by an observer
 *   inside [DisposableEffect] because Composition will be disposed earlier.
 *   But you can check [ScreenLifecycleOwner.isFinishing], it will be true if [Screen] is in the process of being destroyed.
 */
@Composable
@Suppress("NonSkippableComposable")
internal fun AlcubierreRenderImpl(
    navDriveOwner: ComposeNavDriveOwner,
    animationType: Int,
    addTransition: (ScreenTransitionScope.() -> ContentTransform)?,
    removeTransition: (ScreenTransitionScope.() -> ContentTransform)?,
    animationSpec: FiniteAnimationSpec<Float>?
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

    when (animationType) {
        NO_ANIMATION -> {
            ImmediateCurrentScreen(
                currentStateProjection = ScreenRootNavStateProjection(currentState),
                stateHolder = stateHolder,
            )
        }
        GENERIC_ANIMATION -> {
            AnimatedCurrentScreen(
                currentStateProjection = ScreenRootNavStateProjection(currentState),
                stateHolder = stateHolder,
                addTransition = addTransition!!,
                removeTransition = removeTransition!!,
                sameTransitions = false
            )
        }
        GENERIC_ANIMATION_SAME_ENTER_EXIT -> {
            AnimatedCurrentScreen(
                currentStateProjection = ScreenRootNavStateProjection(currentState),
                stateHolder = stateHolder,
                addTransition = addTransition!!,
                removeTransition = removeTransition!!,
                sameTransitions = true
            )
        }
        CROSSFADE -> {
            CrossfadeCurrentScreen(
                currentStateProjection = ScreenRootNavStateProjection(currentState),
                stateHolder = stateHolder,
                animationSpec = animationSpec!!
            )
        }
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
    sameTransitions: Boolean
) {
    val transition = updateTransition(currentStateProjection.state, label = "Alcubierre")
    transition.AnimatedContent(
        contentKey = { state ->
            state.currentScreen?.screenId
        },
        transitionSpec = {
            val transitionScope = ScreenTransitionScope(initialState.currentScreen, targetState.currentScreen)
            // No need to check if the previous screen is removed if the transitions are the same.
            if (sameTransitions || isPreviousScreenRemoved(initialState, targetState)) {
                removeTransition.invoke(transitionScope)
            } else {
                addTransition.invoke(transitionScope)
            }
        }
    ) { targetState ->
        CompositionLocalProvider(LocalRenderAnimatedContentScope provides this) {
            CurrentScreen(ScreenRootNavStateProjection(targetState), stateHolder)
        }
    }
    AnimationEffects(transition)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CrossfadeCurrentScreen(
    currentStateProjection: ScreenRootNavStateProjection,
    stateHolder: ImmutableSaveableStateHolder,
    animationSpec: FiniteAnimationSpec<Float>
) {
    val transition = updateTransition(currentStateProjection.state, label = "Alcubierre")
    transition.Crossfade(
        contentKey = { state ->
            state.currentScreen?.screenId
        },
        animationSpec = animationSpec,
    ) { targetState ->
        CurrentScreen(ScreenRootNavStateProjection(targetState), stateHolder)
    }
    AnimationEffects(transition)
}

@Composable
@NonRestartableComposable
private fun AnimationEffects(transition: Transition<ComposeRootNavState>) {
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
    // When you leave the screen, current transition will be marked completed.
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
        val parentLifecycle = LocalLifecycleOwner.current.lifecycle
        CompositionLocalProvider(*screen.lifecycleManager.providedValues) {
            stateHolder.SaveableStateProvider(screen.screenId) {
                screen.lifecycleManager.LifecycleHandler(parentLifecycle)
                screen.Content()
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

            val parentLifecycle = LocalLifecycleOwner.current.lifecycle
            CompositionLocalProvider(*dialog.lifecycleManager.providedValues) {
                stateHolder.SaveableStateProvider(dialog.dialogId) {
                    dialog.lifecycleManager.LifecycleHandler(parentLifecycle)
                    dialog.Content(dialog.hideRequest, onDismissRequest)
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
        val stack = stackState.stack
        for (i in stack.indices) {
            if (stack[i].screenId == previousScreenId) {
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

    previousDialog.hideRequest.hide()
    previousDialog.lifecycleManager.onRemoved()
    stateHolder.removeState(previousDialog.dialogId)
}

private fun disposeUnusedScreens(
    previous: ComposeRootNavState?,
    current: ComposeRootNavState,
    stateHolder: SaveableStateHolder
) {
    if (previous == null) return

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

    previous.currentScreen?.let { previousScreen ->
        if (previousScreen.screenId != current.currentScreen?.screenId) {
            previousScreen.lifecycleManager.onStacked()
        }
    }
}

internal const val NO_ANIMATION = 0
internal const val GENERIC_ANIMATION = 1
internal const val GENERIC_ANIMATION_SAME_ENTER_EXIT = 2
internal const val CROSSFADE = 3
