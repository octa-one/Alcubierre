package com.github.octaone.alcubierre

import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.state.RootNavState
import android.os.Bundle

/**
 * Базовый интерфейс для навигации.
 *
 * Библиотека состоит из 3 основных классов:
 *  - Reducer: преобразует [RootNavState] в соответствии с [NavAction]
 *  - Render: передаёт команды в android framework (например в FragmentManager), чтобы отобразить актуальный [RootNavState]
 *  - AlcubierreHost: базовый класс навигации, который хранит в себе состояние экранов и являвется Render для [RootNavState]
 *
 * [state] - текущее состояние навигации.
 * [dispatch] - метод для применения [NavAction].
 */
interface NavDrive {

    val state: RootNavState
    fun dispatch(action: NavAction)
}

/**
 * Базовый интерфейс для хоста навигации.
 */
interface NavDriveOwner : NavDrive {

    fun onResume()

    fun onPause()

    fun saveState(outState: Bundle)
}
