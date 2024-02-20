package com.github.octaone.alcubierre.screen

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.github.octaone.alcubierre.lifecycle.ScreenLifecycleManager
import com.github.octaone.alcubierre.lifecycle.ScreenLifecycleManagerImpl
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import com.github.octaone.alcubierre.screen.extra.LazyExtrasContainer

@Stable
abstract class ComposeDialog : Dialog(), ExtrasContainer by LazyExtrasContainer() {

    val lifecycleOwner: ScreenLifecycleManager by lazy(LazyThreadSafetyMode.NONE) {
        ScreenLifecycleManagerImpl(dialogId, getSavedStateDefaultArguments())
    }

    override val priority: Int = 5

    open fun getSavedStateDefaultArguments(): Bundle? = null

    @Composable
    abstract fun Content()
}
