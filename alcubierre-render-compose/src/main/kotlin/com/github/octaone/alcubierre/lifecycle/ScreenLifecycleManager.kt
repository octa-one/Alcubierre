package com.github.octaone.alcubierre.lifecycle

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner

interface ScreenLifecycleManager :
    LifecycleOwner,
    ViewModelStoreOwner,
    SavedStateRegistryOwner,
    HasDefaultViewModelProviderFactory {

    val id: String

    var parentLifecycleState: Lifecycle.State

    fun setApplication(app: Application?)

    fun handleLifecycleEvent(event: Lifecycle.Event)

    fun updateLifecycleState(state: Lifecycle.State)

    fun performRestore(savedState: Bundle?)

    fun performSave(outState: Bundle)
}
