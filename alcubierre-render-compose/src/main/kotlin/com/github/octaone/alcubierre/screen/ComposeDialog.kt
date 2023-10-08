package com.github.octaone.alcubierre.screen

import android.os.Bundle
import androidx.compose.runtime.Composable
import com.github.octaone.alcubierre.lifecycle.ScreenLifecycleOwner
import com.github.octaone.alcubierre.lifecycle.ScreenLifecycleOwnerImpl
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import com.github.octaone.alcubierre.screen.extra.LazyExtrasContainer

abstract class ComposeDialog : Dialog(), ExtrasContainer by LazyExtrasContainer() {

    val lifecycleOwner: ScreenLifecycleOwner by lazy(LazyThreadSafetyMode.NONE) {
        ScreenLifecycleOwnerImpl(dialogId, getSavedStateDefaultArguments())
    }

    override val priority: Int = 5

    open fun getSavedStateDefaultArguments(): Bundle? = null

    @Composable
    abstract fun Content()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ComposeDialog

        if (dialogId != other.dialogId) return false

        return true
    }

    override fun hashCode(): Int = this::class.java.simpleName.hashCode()
}
