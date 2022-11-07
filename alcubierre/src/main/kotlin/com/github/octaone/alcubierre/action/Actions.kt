package com.github.octaone.alcubierre.action

import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.state.RootNavState

class ShowDialog(val dialog: Dialog) : NavAction

object DismissDialog : NavAction

class Forward(val screens: List<Screen>) : NavAction

class Replace(val screens: List<Screen>) : NavAction

class ReplaceRoot(val screens: List<Screen>) : NavAction

class NewStack(val stackId: Int, val screens: List<Screen>) : NavAction

class SelectStack(val stackId: Int) : NavAction

class ClearStack(val stackId: Int) : NavAction

class BackTo(val screenId: String) : NavAction

object BackToRoot : NavAction

object Back : NavAction

class ApplyState(val state: RootNavState) : NavAction

/**
 * Открытие следующего экрана [screens]
 */
fun NavDrive.forward(vararg screens: Screen) = dispatch(Forward(screens.toList()))

/**
 * Замена текущего экрана на экран(ы) [screens]
 */
fun NavDrive.replace(vararg screens: Screen) = dispatch(Replace(screens.toList()))

/**
 * Замена всего стека с корневого экрана на экран(ы) [screens]
 */
fun NavDrive.replaceRoot(vararg screens: Screen) = dispatch(ReplaceRoot(screens.toList()))

/**
 * Добавление нового стека
 */
fun NavDrive.newStack(stackId: Int, vararg screens: Screen) = dispatch(NewStack(stackId, screens.toList()))

/**
 * Выбор активного стека
 */
fun NavDrive.selectStack(stackId: Int) = dispatch(SelectStack(stackId))

/**
 * Удаление стека
 */
fun NavDrive.clearStack(stackId: Int) = dispatch(ClearStack(stackId))

/**
 * Переход назад к экрану [screenId]
 */
fun NavDrive.backTo(screenId: String) = dispatch(BackTo(screenId))

/**
 * Переход назад к корневого экрану стека
 */
fun NavDrive.backToRoot() = dispatch(BackToRoot)

/**
 * Переход назад
 */
fun NavDrive.back() = dispatch(Back)

/**
 * Применение нового состония
 */
fun NavDrive.applyState(state: RootNavState) = dispatch(ApplyState(state))

/**
 * Показ диалога
 */
fun NavDrive.showDialog(dialog: Dialog) = dispatch(ShowDialog(dialog))

/**
 * Скрытие диалога
 */
fun NavDrive.dismissDialog() = dispatch(DismissDialog)
