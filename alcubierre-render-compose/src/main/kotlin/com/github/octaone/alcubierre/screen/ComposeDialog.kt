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
abstract class ComposeDialog(
    val composeContentName: String,
    val composeContentClass: Class<out ComposeDialogContent<*>>?
) : Dialog(), ExtrasContainer by LazyExtrasContainer() {

    constructor() : this("", null) { require(this is ComposeDialogContent<*>) }
    constructor(contentName: String) : this(contentName, null)
    constructor(contentClass: KClass<out ComposeDialogContent<*>>) : this(contentClass.java.name, contentClass.java)

    internal var content: ComposeDialogContent<*>? = null

    open val lifecycleManager: DialogLifecycleManager by lazy(LazyThreadSafetyMode.NONE) {
        DefaultDialogLifecycleManager(dialogId, getSavedStateDefaultArguments())
    }

    override val priority: Int = 5

    open fun getSavedStateDefaultArguments(): Bundle? = null

    internal val hideRequest = HideRequest()

    suspend fun hide() {
        hideRequest.hideAndAwaitHidden()
    }
}

interface ComposeDialogContent<S : ComposeDialog> {

    @Composable
    fun S.Content(hideRequest: HideRequest, onDismissRequest: () -> Unit)
}
