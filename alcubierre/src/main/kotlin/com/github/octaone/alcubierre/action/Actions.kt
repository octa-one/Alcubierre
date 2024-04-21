package com.github.octaone.alcubierre.action

import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.state.RootNavState

public data class ShowDialog<S : Screen, D : Dialog>(val dialog: D) : NavAction<S, D>

public data object DismissDialog : NavAction<Nothing, Nothing>

public data class Forward<S : Screen, D : Dialog>(val screens: List<S>) : NavAction<S, D>

public data class Replace<S : Screen, D : Dialog>(val screens: List<S>) : NavAction<S, D>

public data class ReplaceRoot<S : Screen, D : Dialog>(val screens: List<S>) : NavAction<S, D>

public data class NewStack<S : Screen, D : Dialog>(val stackId: Int, val screens: List<S>) : NavAction<S, D>

public data class SelectStack<S : Screen, D : Dialog>(val stackId: Int) : NavAction<S, D>

public data class ClearStack<S : Screen, D : Dialog>(val stackId: Int) : NavAction<S, D>

public data class BackTo<S : Screen, D : Dialog>(val screen: S, val inclusive: Boolean) : NavAction<S, D>

public data object BackToRoot : NavAction<Nothing, Nothing>

public data object Back : NavAction<Nothing, Nothing>

public data class ApplyState<S : Screen, D : Dialog>(val state: RootNavState<S, D>) : NavAction<S, D>

public data class Batch<S : Screen, D : Dialog>(val actions: List<NavAction<S, D>>) : NavAction<S, D>

/**
 * Open next [screens]
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.forward(vararg screens: S): Unit =
    dispatch(Forward(screens.toList()))

/**
 * Replace current screen by new [screens]
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.replace(vararg screens: S): Unit =
    dispatch(Replace(screens.toList()))

/**
 * Replace entire stack including root with [screens]
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.replaceRoot(vararg screens: S): Unit =
    dispatch(ReplaceRoot(screens.toList()))

/**
 * Add new stack with [stackId] and [screens]
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.newStack(stackId: Int, vararg screens: S): Unit =
    dispatch(NewStack(stackId, screens.toList()))

/**
 * Select active stack by [stackId]
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.selectStack(stackId: Int): Unit =
    dispatch(SelectStack(stackId))

/**
 * Clear stack by [stackId]
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.clearStack(stackId: Int): Unit =
    dispatch(ClearStack(stackId))

/**
 * Back to [screen]
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.backTo(screen: S, inclusive: Boolean = false): Unit =
    dispatch(BackTo(screen, inclusive))

/**
 * Back to root screen
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.backToRoot(): Unit =
    dispatch(BackToRoot)

/**
 * Go back
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.back(): Unit =
    dispatch(Back)

/**
 * Apply new state [state]
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.applyState(state: RootNavState<S, D>): Unit =
    dispatch(ApplyState(state))

/**
 * Show [dialog]
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.showDialog(dialog: D): Unit =
    dispatch(ShowDialog(dialog))

/**
 * Dismiss dialog
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.dismissDialog(): Unit =
    dispatch(DismissDialog)

/**
 * Batch multiple actions
 */
public fun <S : Screen, D : Dialog>  NavDrive<S, D>.batch(block: NavDrive<S, D>.() -> Unit): Unit =
    dispatch(Batch(NavDriveBatchRecorder(state).apply(block).actions))
