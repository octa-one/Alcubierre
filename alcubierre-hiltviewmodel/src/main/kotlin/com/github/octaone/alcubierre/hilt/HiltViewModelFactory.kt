package com.github.octaone.alcubierre.hilt

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory

/**
 * Creates a [ViewModelProvider.Factory] to get
 * [HiltViewModel](https://dagger.dev/api/latest/dagger/hilt/android/lifecycle/HiltViewModel)
 * -annotated `ViewModel` from a [ViewModelProvider.Factory].
 *
 * @param context the activity context.
 * @param delegateFactory the delegated factory.
 * @return the factory.
 * @throws IllegalStateException if the context given is not an activity.
 */
internal fun HiltViewModelFactory(
    context: Context,
    delegateFactory: ViewModelProvider.Factory
): ViewModelProvider.Factory {
    val activity = context.let {
        var ctx = it
        while (ctx is ContextWrapper) {
            // Hilt can only be used with ComponentActivity
            if (ctx is ComponentActivity) {
                return@let ctx
            }
            ctx = ctx.baseContext
        }
        throw IllegalStateException(
            "Expected an activity context for creating a HiltViewModelFactory " +
                    "but instead found: $ctx"
        )
    }
    return HiltViewModelFactory.createInternal(
        /* activity = */ activity,
        /* delegateFactory = */ delegateFactory
    )
}
