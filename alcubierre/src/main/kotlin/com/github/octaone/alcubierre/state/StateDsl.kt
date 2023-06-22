package com.github.octaone.alcubierre.state

import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import kotlin.collections.set

class StackStateBuilder<S : Screen>  {

    private val screens = mutableListOf<S>()

    fun screen(screen: S) {
        screens.add(screen)
    }

    fun build(): StackNavState<S> =
        StackNavState(screens.toList())
}

class RootStateBuilder<S : Screen, D : Dialog>  {

    private val stacks = HashMap<Int, StackNavState<S>>()
    private var stackId: Int? = null

    fun stack(id: Int, builder: StackStateBuilder<S>.() -> Unit) {
        if (stackId == null) stackId = id
        stacks[id] = StackStateBuilder<S>().apply(builder).build()
    }

    fun selectStack(id: Int) {
        stackId = id
    }

    fun build(): RootNavState<S, D> =
        RootNavState(DialogNavState.EMPTY, stacks, checkNotNull(stackId))
}

fun <S : Screen, D : Dialog> rootState(builder: RootStateBuilder<S, D>.() -> Unit): RootNavState<S, D> =
    RootStateBuilder<S, D>().apply(builder).build()

fun <S : Screen, D : Dialog> singleRootState(builder: StackStateBuilder<S>.() -> Unit): RootNavState<S, D> =
    RootStateBuilder<S, D>()
        .apply { stack(SINGLE_STACK_ID, builder) }
        .build()

internal const val SINGLE_STACK_ID = 100
