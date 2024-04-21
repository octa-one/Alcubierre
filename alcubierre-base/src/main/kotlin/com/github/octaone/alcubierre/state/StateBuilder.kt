package com.github.octaone.alcubierre.state

import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import kotlin.collections.set

public class StackStateBuilder<S : Screen>  {

    private val screens = mutableListOf<S>()

    public fun screen(screen: S) {
        screens.add(screen)
    }

    public fun build(): StackNavState<S> =
        StackNavState(screens.toList())
}

public class RootStateBuilder<S : Screen, D : Dialog>  {

    private val stacks = HashMap<Int, StackNavState<S>>()
    private var stackId: Int? = null

    public inline fun stack(id: Int, builder: StackStateBuilder<S>.() -> Unit) {
        stack(id, StackStateBuilder<S>().apply(builder).build())
    }

    @PublishedApi
    internal fun stack(id: Int, state: StackNavState<S>) {
        if (stackId == null) stackId = id
        stacks[id] = state
    }

    public fun selectStack(id: Int) {
        stackId = id
    }

    public fun build(): RootNavState<S, D> =
        RootNavState(DialogNavState.EMPTY, stacks, checkNotNull(stackId))
}

public inline fun <S : Screen, D : Dialog> rootState(builder: RootStateBuilder<S, D>.() -> Unit): RootNavState<S, D> =
    RootStateBuilder<S, D>().apply(builder).build()

public inline fun <S : Screen, D : Dialog> singleStackRootState(
    stackId: Int = SINGLE_STACK_ID,
    builder: StackStateBuilder<S>.() -> Unit
): RootNavState<S, D> =
    RootStateBuilder<S, D>()
        .apply { stack(stackId, builder) }
        .build()

public const val SINGLE_STACK_ID: Int = 100
