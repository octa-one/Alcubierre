@file:Suppress("LeakingThis")

package com.github.octaone.alcubierre.screen

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.github.octaone.alcubierre.lifecycle.DefaultDialogLifecycleManager
import com.github.octaone.alcubierre.lifecycle.DialogLifecycleManager
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import com.github.octaone.alcubierre.screen.extra.LazyExtrasContainer
import kotlin.reflect.KClass

@Stable
public abstract class ComposeDialog(
    public val composeContentName: String,
    public val composeContentClass: Class<out ComposeDialogContent<*>>?
) : Dialog(), ExtrasContainer by LazyExtrasContainer() {

    public constructor() : this("", null) { require(this is ComposeDialogContent<*>) }
    public constructor(contentName: String) : this(contentName, null)
    public constructor(contentClass: KClass<out ComposeDialogContent<*>>) : this(contentClass.java.name, contentClass.java)

    internal var content: ComposeDialogContent<*>? = null

    internal val hideRequest = HideRequest()

    public open val lifecycleManager: DialogLifecycleManager by lazy(LazyThreadSafetyMode.NONE) {
        DefaultDialogLifecycleManager(dialogId, getSavedStateDefaultArguments())
    }

    override val priority: Int = 5

    public open fun getSavedStateDefaultArguments(): Bundle? = null

    public suspend fun hide() {
        hideRequest.hideAndAwaitHidden()
    }
}

public interface ComposeDialogContent<S : ComposeDialog> {

    @Composable
    public fun S.Content(hideRequest: HideRequest, onDismissRequest: () -> Unit)
}
