package com.github.octaone.alcubierre.lifecycle

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

interface LifecycleManager {

    val key: String

    fun initialize(application: Application, savedState: Bundle)

    fun onLaunched(parentLifecycleState: Lifecycle.State)

    fun onParentLifecycleStateChanged(state: Lifecycle.State)
}

@Composable
@NonRestartableComposable
internal fun LifecycleManager.LifecycleHandler(parentLifecycle: Lifecycle) {
    val savedState = rememberSaveable(key = "$key:bundle") { Bundle() }
    val context = LocalContext.current

    initialize(context.applicationContext as Application, savedState)
    onLaunched(parentLifecycle.currentState)

    DisposableEffect(Unit) {
        val parentObserver = LifecycleEventObserver { owner, event ->
            if (event == Lifecycle.Event.ON_DESTROY && context.isChangingConfigurations()) {
                /**
                 * Instance of the screen isn't recreated during config changes so skip this event
                 * to avoid crash while accessing to ViewModel with SavedStateHandle, because after
                 * ON_DESTROY, [androidx.lifecycle.SavedStateHandleController] is marked as not
                 * attached and next call of [registerSavedStateProvider] after recreating Activity
                 * on the same instance causing the crash.
                 */
                return@LifecycleEventObserver
            }

            onParentLifecycleStateChanged(owner.lifecycle.currentState)
        }

        parentLifecycle.addObserver(parentObserver)

        onDispose {
            parentLifecycle.removeObserver(parentObserver)
        }
    }
}

private fun Context.isChangingConfigurations(): Boolean =
    findActivity()?.isChangingConfigurations == true

private tailrec fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
