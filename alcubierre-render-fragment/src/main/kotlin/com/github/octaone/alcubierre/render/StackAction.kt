package com.github.octaone.alcubierre.render

import com.github.octaone.alcubierre.screen.Screen

/**
 * Операции, возможные со стеком.
 */
interface StackAction
data class Pop(val count: Int) : StackAction
data class Push(val screens: List<Screen>) : StackAction
