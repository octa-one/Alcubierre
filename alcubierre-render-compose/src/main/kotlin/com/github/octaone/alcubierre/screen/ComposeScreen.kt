package com.github.octaone.alcubierre.screen

import androidx.compose.runtime.Composable

abstract class ComposeScreen : Screen {

    @Composable
    abstract fun Content()

    override val screenId: String by lazy(LazyThreadSafetyMode.NONE) { "${this::class.java.simpleName}_${hashCode()}" }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return true
    }

    override fun hashCode(): Int = this::class.java.simpleName.hashCode()
}
