@file:Suppress("LeakingThis")

package com.github.octaone.alcubierre.screen

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.github.octaone.alcubierre.lifecycle.DefaultScreenLifecycleManager
import com.github.octaone.alcubierre.lifecycle.ScreenLifecycleManager
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import com.github.octaone.alcubierre.screen.extra.LazyExtrasContainer
import kotlin.reflect.KClass

@Stable
abstract class ComposeScreen(
    val composeContentName: String,
    val composeContentClass: Class<out ComposeScreenContent<*>>?
) : Screen(), ExtrasContainer by LazyExtrasContainer() {

    constructor() : this("", null) { require(this is ComposeScreenContent<*>) }
    constructor(contentName: String) : this(contentName, null)
    constructor(contentClass: KClass<out ComposeScreenContent<*>>) : this(contentClass.java.name, contentClass.java)

    internal var content: ComposeScreenContent<*>? = null

    open val lifecycleManager: ScreenLifecycleManager by lazy(LazyThreadSafetyMode.NONE) {
        DefaultScreenLifecycleManager(screenId, getSavedStateDefaultArguments())
    }

    open fun getSavedStateDefaultArguments(): Bundle? = null
}

interface ComposeScreenContent<S : ComposeScreen> {

    @Composable
    fun S.Content()
}
