package space.octaone.alcubierre.render

import space.octaone.alcubierre.screen.FragmentScreen

internal interface StackAction

internal data class Pop(val count: Int) : StackAction

internal data class Push(val screens: List<FragmentScreen>) : StackAction
