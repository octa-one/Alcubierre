package com.github.octaone.alcubierre.state

import com.github.octaone.alcubierre.screen.Screen
import kotlin.collections.set

class StackStateBuilder {

    private val screens = mutableListOf<Screen>()

    fun screen(screen: Screen) {
        screens.add(screen)
    }

    fun build(): StackNavigationState =
        StackNavigationState(screens.toList())
}

class RootStateBuilder {

    private val stacks = HashMap<Int, StackNavigationState>()
    private var stackId: Int? = null

    fun stack(id: Int, builder: StackStateBuilder.() -> Unit) {
        if (stackId == null) stackId = id
        stacks[id] = StackStateBuilder().apply(builder).build()
    }

    fun selectStack(id: Int) {
        stackId = id
    }

    fun build(): RootNavigationState =
        RootNavigationState(null, stacks, checkNotNull(stackId))
}

fun rootState(builder: RootStateBuilder.() -> Unit): RootNavigationState =
    RootStateBuilder().apply(builder).build()

fun singleRootState(builder: StackStateBuilder.() -> Unit): RootNavigationState =
    RootStateBuilder()
        .apply { stack(SINGLE_STACK_ID, builder) }
        .build()

internal const val SINGLE_STACK_ID = 100
