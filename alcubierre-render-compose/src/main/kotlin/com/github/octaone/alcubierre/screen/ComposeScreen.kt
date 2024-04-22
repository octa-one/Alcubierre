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
public abstract class ComposeScreen(
    public val composeContentName: String,
    public val composeContentClass: Class<out ComposeScreenContent<*>>?
) : Screen(), ExtrasContainer by LazyExtrasContainer() {

    public constructor() : this("", null) { require(this is ComposeScreenContent<*>) }
    public constructor(contentName: String) : this(contentName, null)
    public constructor(contentClass: KClass<out ComposeScreenContent<*>>) : this(contentClass.java.name, contentClass.java)

    internal var content: ComposeScreenContent<*>? = null

    public open val lifecycleManager: ScreenLifecycleManager by lazy(LazyThreadSafetyMode.NONE) {
        DefaultScreenLifecycleManager(screenId, getSavedStateDefaultArguments())
    }

    public open fun getSavedStateDefaultArguments(): Bundle? = null
}

public interface ComposeScreenContent<S : ComposeScreen> {

    @Composable
    public fun S.Content()
}
