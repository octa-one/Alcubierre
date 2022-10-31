package com.github.octaone.alcubierre.action

import com.github.octaone.alcubierre.Alcubierre
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.state.RootNavigationState

interface NavigationAction

class ShowDialog(val dialog: Dialog) : NavigationAction
object DismissDialog : NavigationAction

class Forward(val screens: List<Screen>) : NavigationAction
class Replace(val screens: List<Screen>) : NavigationAction
class ReplaceRoot(val screens: List<Screen>) : NavigationAction
class NewStack(val stackId: Int, val screens: List<Screen>) : NavigationAction
class SelectStack(val stackId: Int) : NavigationAction
class BackTo(val screenId: String) : NavigationAction
object BackToRoot : NavigationAction
object Back : NavigationAction
class ApplyState(val state: RootNavigationState) : NavigationAction

/**
 * Открытие следующего экрана [screens]
 */
fun Alcubierre.forward(vararg screens: Screen) = dispatch(Forward(screens.toList()))

/**
 * Замена текущего экрана на экран(ы) [screens]
 */
fun Alcubierre.replace(vararg screens: Screen) = dispatch(Replace(screens.toList()))

/**
 * Замена всего стека с корневого экрана на экран(ы) [screens]
 */
fun Alcubierre.replaceRoot(vararg screens: Screen) = dispatch(ReplaceRoot(screens.toList()))

/**
 * Добавление нового стека
 */
fun Alcubierre.newStack(stackId: Int, vararg screens: Screen) = dispatch(NewStack(stackId, screens.toList()))

/**
 * Выбор активного стека
 */
fun Alcubierre.selectStack(stackId: Int) = dispatch(SelectStack(stackId))

/**
 * Переход назад к экрану [screenId]
 */
fun Alcubierre.backTo(screenId: String) = dispatch(BackTo(screenId))

/**
 * Переход назад к корневого экрану стека
 */
fun Alcubierre.backToRoot() = dispatch(BackToRoot)

/**
 * Переход назад
 */
fun Alcubierre.back() = dispatch(Back)

/**
 * Применение нового состония
 */
fun Alcubierre.applyState(state: RootNavigationState) = dispatch(ApplyState(state))

/**
 * Показ диалога
 */
fun Alcubierre.showDialog(dialog: Dialog) = dispatch(ShowDialog(dialog))

/**
 * Скрытие диалога
 */
fun Alcubierre.dismissDialog() = dispatch(DismissDialog)
