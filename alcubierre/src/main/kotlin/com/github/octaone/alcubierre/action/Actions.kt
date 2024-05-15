package com.github.octaone.alcubierre.action

import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.reduce.BatchRootNavReducer
import com.github.octaone.alcubierre.reduce.DialogNavReducer
import com.github.octaone.alcubierre.reduce.DialogRootNavReducer
import com.github.octaone.alcubierre.reduce.ScreenRootNavReducer
import com.github.octaone.alcubierre.reduce.ScreenStackNavReducer
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.state.RootNavState

/**
 * Action to show a [dialog].
 * @see DialogRootNavReducer
 * @see DialogNavReducer
 */
public data class ShowDialog<S : Screen, D : Dialog>(val dialog: D) : NavAction<S, D>

/**
 * Action to dismiss current dialog.
 * @see DialogRootNavReducer
 * @see DialogNavReducer
 */
public data object DismissDialog : NavAction<Nothing, Nothing>

/**
 * Action to add [screens] to the current backstack.
 * @see ScreenRootNavReducer
 * @see ScreenStackNavReducer
 */
public data class Forward<S : Screen, D : Dialog>(val screens: List<S>) : NavAction<S, D>

/**
 * Action to replace current (last) screen with new [screens] in the current backstack.
 * @see ScreenRootNavReducer
 * @see ScreenStackNavReducer
 */
public data class Replace<S : Screen, D : Dialog>(val screens: List<S>) : NavAction<S, D>

/**
 * Action to replace current stack from the root with new [screens].
 * @see ScreenRootNavReducer
 * @see ScreenStackNavReducer
 */
public data class ReplaceRoot<S : Screen, D : Dialog>(val screens: List<S>) : NavAction<S, D>

/**
 * Action to add a new stack [stackId] with [screens].
 * @see ScreenRootNavReducer
 */
public data class NewStack<S : Screen, D : Dialog>(val stackId: Int, val screens: List<S>) : NavAction<S, D>

/**
 * Action to switch to the stack with id [stackId].
 * @see ScreenRootNavReducer
 */
public data class SelectStack<S : Screen, D : Dialog>(val stackId: Int) : NavAction<S, D>

/**
 * Action to remove to the stack with id [stackId].
 * Do not clear the current stack. You can use [batch] to clear and switch the stack in a single dispatch call.
 * @see ScreenRootNavReducer
 */
public data class ClearStack<S : Screen, D : Dialog>(val stackId: Int) : NavAction<S, D>

/**
 * Action to pop current stack to the [screen].
 * If this operation is [inclusive], then [screen] will also be popped.
 * @see ScreenRootNavReducer
 * @see ScreenStackNavReducer
 */
public data class BackTo<S : Screen, D : Dialog>(val screen: S, val inclusive: Boolean) : NavAction<S, D>

/**
 * Action to pop current stack to the root (first screen).
 * @see ScreenRootNavReducer
 * @see ScreenStackNavReducer
 */
public data object BackToRoot : NavAction<Nothing, Nothing>

/**
 * Action to pop current stack to previous screen.
 * If there is only one (root) element in the stack, nothing will happen.
 * @see ScreenRootNavReducer
 * @see ScreenStackNavReducer
 */
public data object Back : NavAction<Nothing, Nothing>

/**
 * Action to replace current state with the new [state].
 * Do not use it to modify state, use specific actions and [batch] in more complex cases.
 * Updating the state bypassing reducers can lead to bugs, because some reducers can have side effects based on actions.
 * @see ScreenRootNavReducer
 */
public data class ApplyState<S : Screen, D : Dialog>(val state: RootNavState<S, D>) : NavAction<S, D>

/**
 * Action to batch multiple [actions] to be used later in a single dispatch call.
 * @see BatchRootNavReducer
 */
public data class Batch<S : Screen, D : Dialog>(val actions: List<NavAction<S, D>>) : NavAction<S, D>

/**
 * Extension to dispatch action [Forward].
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.forward(vararg screens: S): Unit =
    dispatch(Forward(screens.toList()))

/**
 * Extension to dispatch action [Replace].
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.replace(vararg screens: S): Unit =
    dispatch(Replace(screens.toList()))

/**
 * Extension to dispatch action [ReplaceRoot].
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.replaceRoot(vararg screens: S): Unit =
    dispatch(ReplaceRoot(screens.toList()))

/**
 * Extension to dispatch action [NewStack].
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.newStack(stackId: Int, vararg screens: S): Unit =
    dispatch(NewStack(stackId, screens.toList()))

/**
 * Extension to dispatch action [SelectStack].
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.selectStack(stackId: Int): Unit =
    dispatch(SelectStack(stackId))

/**
 * Extension to dispatch action [ClearStack].
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.clearStack(stackId: Int): Unit =
    dispatch(ClearStack(stackId))

/**
 * Extension to dispatch action [BackTo].
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.backTo(screen: S, inclusive: Boolean = false): Unit =
    dispatch(BackTo(screen, inclusive))

/**
 * Extension to dispatch action [BackToRoot].
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.backToRoot(): Unit =
    dispatch(BackToRoot)

/**
 * Extension to dispatch action [Back].
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.back(): Unit =
    dispatch(Back)

/**
 * Extension to dispatch action [ApplyState].
 * Do not use it to modify state, use specific actions and [batch] in more complex cases.
 * Updating the state bypassing reducers can lead to bugs, because some reducers can have side effects based on actions.
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.applyState(state: RootNavState<S, D>): Unit =
    dispatch(ApplyState(state))

/**
 * Extension to dispatch action [ShowDialog].
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.showDialog(dialog: D): Unit =
    dispatch(ShowDialog(dialog))

/**
 * Extension to dispatch action [DismissDialog].
 */
public fun <S : Screen, D : Dialog> NavDrive<S, D>.dismissDialog(): Unit =
    dispatch(DismissDialog)

/**
 * Extension to dispatch action [Batch].
 * All actions inside the [block] will be dispatched simultaneously.
 */
public fun <S : Screen, D : Dialog>  NavDrive<S, D>.batch(block: NavDrive<S, D>.() -> Unit): Unit =
    dispatch(Batch(NavDriveBatchRecorder(state).apply(block).actions))
