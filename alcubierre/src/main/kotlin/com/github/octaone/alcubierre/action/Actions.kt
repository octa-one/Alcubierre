package com.github.octaone.alcubierre.action

import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.state.RootNavState

class ShowDialog<S : Screen, D : Dialog>(val dialog: D) : NavAction<S, D>

object DismissDialog : NavAction<Nothing, Nothing>

class Forward<S : Screen, D : Dialog>(val screens: List<S>) : NavAction<S, D>

class Replace<S : Screen, D : Dialog>(val screens: List<S>) : NavAction<S, D>

class ReplaceRoot<S : Screen, D : Dialog>(val screens: List<S>) : NavAction<S, D>

class NewStack<S : Screen, D : Dialog>(val stackId: Int, val screens: List<S>) : NavAction<S, D>

class SelectStack<S : Screen, D : Dialog>(val stackId: Int) : NavAction<S, D>

class ClearStack<S : Screen, D : Dialog>(val stackId: Int) : NavAction<S, D>

class BackTo<S : Screen, D : Dialog>(val screen: S) : NavAction<S, D>

object BackToRoot : NavAction<Nothing, Nothing>

object Back : NavAction<Nothing, Nothing>

class ApplyState<S : Screen, D : Dialog>(val state: RootNavState<S, D>) : NavAction<S, D>

class Batch<S : Screen, D : Dialog>(val actions: List<NavAction<S, D>>) : NavAction<S, D>

/**
 * Open next [screens]
 */
fun <S : Screen, D : Dialog> NavDrive<S, D>.forward(vararg screens: S) = dispatch(Forward<S, D>(screens.toList()))

/**
 * Replace current screen by new [screens]
 */
fun <S : Screen, D : Dialog> NavDrive<S, D>.replace(vararg screens: S) = dispatch(Replace<S, D>(screens.toList()))

/**
 * Replace entire stack including root with [screens]
 */
fun <S : Screen, D : Dialog> NavDrive<S, D>.replaceRoot(vararg screens: S) = dispatch(ReplaceRoot<S, D>(screens.toList()))

/**
 * Add new stack with [stackId] and [screens]
 */
fun <S : Screen, D : Dialog> NavDrive<S, D>.newStack(stackId: Int, vararg screens: S) = dispatch(NewStack<S, D>(stackId, screens.toList()))

/**
 * Select active stack by [stackId]
 */
fun <S : Screen, D : Dialog> NavDrive<S, D>.selectStack(stackId: Int) = dispatch(SelectStack<S, D>(stackId))

/**
 * Clear stack by [stackId]
 */
fun <S : Screen, D : Dialog> NavDrive<S, D>.clearStack(stackId: Int) = dispatch(ClearStack<S, D>(stackId))

/**
 * Back to [screen]
 */
fun <S : Screen, D : Dialog> NavDrive<S, D>.backTo(screen: S) = dispatch(BackTo<S, D>(screen))

/**
 * Back to root screen
 */
fun <S : Screen, D : Dialog> NavDrive<S, D>.backToRoot() = dispatch(BackToRoot)

/**
 * Go back
 */
fun <S : Screen, D : Dialog> NavDrive<S, D>.back() = dispatch(Back)

/**
 * Apply new state [state]
 */
fun <S : Screen, D : Dialog> NavDrive<S, D>.applyState(state: RootNavState<S, D>) = dispatch(ApplyState(state))

/**
 * Show [dialog]
 */
fun <S : Screen, D : Dialog> NavDrive<S, D>.showDialog(dialog: D) = dispatch(ShowDialog(dialog))

/**
 * Dismiss dialog
 */
fun <S : Screen, D : Dialog> NavDrive<S, D>.dismissDialog() = dispatch(DismissDialog)

/**
 * Batch multiple actions
 */
fun <S : Screen, D : Dialog>  NavDrive<S, D>.batch(block: NavDrive<S, D>.() -> Unit) {
    dispatch(Batch(NavDriveBatchRecorder(state).apply(block).actions))
}
