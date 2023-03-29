package com.github.octaone.alcubierre.action

import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.reduce.NavReducer
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.ScreenId
import com.github.octaone.alcubierre.state.RootNavState

class ShowDialog(val dialog: Dialog) : NavAction

object DismissDialog : NavAction

class Forward(val screens: List<Screen>) : NavAction

class Replace(val screens: List<Screen>) : NavAction

class ReplaceRoot(val screens: List<Screen>) : NavAction

class NewStack(val stackId: Int, val screens: List<Screen>) : NavAction

class SelectStack(val stackId: Int) : NavAction

class ClearStack(val stackId: Int) : NavAction

class BackTo(val screenId: ScreenId) : NavAction

object BackToRoot : NavAction

object Back : NavAction

class ApplyState(val state: RootNavState) : NavAction
class Batch(val actions: List<NavAction>) : NavAction

/**
 * Open next [screens]
 */
fun NavDrive.forward(vararg screens: Screen) = dispatch(Forward(screens.toList()))

/**
 * Replace current screen by new [screens]
 */
fun NavDrive.replace(vararg screens: Screen) = dispatch(Replace(screens.toList()))

/**
 * Replace entire stack including root with [screens]
 */
fun NavDrive.replaceRoot(vararg screens: Screen) = dispatch(ReplaceRoot(screens.toList()))

/**
 * Add new stack with [stackId] and [screens]
 */
fun NavDrive.newStack(stackId: Int, vararg screens: Screen) = dispatch(NewStack(stackId, screens.toList()))

/**
 * Select active stack by [stackId]
 */
fun NavDrive.selectStack(stackId: Int) = dispatch(SelectStack(stackId))

/**
 * Clear stack by [stackId]
 */
fun NavDrive.clearStack(stackId: Int) = dispatch(ClearStack(stackId))

/**
 * Back to screen with [screenId]
 */
fun NavDrive.backTo(screenId: ScreenId) = dispatch(BackTo(screenId))

/**
 * Back to root screen
 */
fun NavDrive.backToRoot() = dispatch(BackToRoot)

/**
 * Go back
 */
fun NavDrive.back() = dispatch(Back)

/**
 * Apply new state [state]
 */
fun NavDrive.applyState(state: RootNavState) = dispatch(ApplyState(state))

/**
 * Show [dialog]
 */
fun NavDrive.showDialog(dialog: Dialog) = dispatch(ShowDialog(dialog))

/**
 * Dismiss dialog
 */
fun NavDrive.dismissDialog() = dispatch(DismissDialog)

/**
 * Batch multiple actions
 */
fun NavDrive.batch(block: NavDrive.() -> Unit) {
    dispatch(Batch(NavDriveBatchRecorder(state).apply(block).actions))
}
