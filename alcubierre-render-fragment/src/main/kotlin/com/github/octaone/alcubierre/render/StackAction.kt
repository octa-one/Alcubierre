package com.github.octaone.alcubierre.render

import com.github.octaone.alcubierre.screen.FragmentScreen

/**
 * Stack operations
 */
internal interface StackAction

internal data class Pop(val count: Int) : StackAction

internal data class Push(val screens: List<FragmentScreen>) : StackAction
