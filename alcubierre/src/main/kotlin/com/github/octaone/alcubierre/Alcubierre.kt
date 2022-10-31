package com.github.octaone.alcubierre

import com.github.octaone.alcubierre.action.NavigationAction
import com.github.octaone.alcubierre.state.RootNavigationState
import android.os.Bundle

/**
 * Базовый интерфейс для навигации.
 *
 * Библиотека состоит из 3 основных классов:
 *  - Reducer: преобразует [RootNavigationState] в соответствии с [NavigationAction]
 *  - Render: передаёт команды в android framework (например в FragmentManager), чтобы отобразить актуальный [RootNavigationState]
 *  - AlcubierreHost: базовый класс навигации, который хранит в себе состояние экранов и являвется Render для [RootNavigationState]
 *
 * [state] - текущее состояние навигации.
 * [dispatch] - метод для применения [NavigationAction].
 */
interface Alcubierre {

    val state: RootNavigationState
    fun dispatch(action: NavigationAction)
}

/**
 * Базовый интерфейс для хоста навигации.
 */
interface AlcubierreHost : Alcubierre {

    fun onResume()

    fun onPause()

    fun saveState(outState: Bundle)
}
