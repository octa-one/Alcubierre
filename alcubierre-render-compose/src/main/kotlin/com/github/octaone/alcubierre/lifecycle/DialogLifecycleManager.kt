package com.github.octaone.alcubierre.lifecycle

import android.app.Application
import android.os.Bundle
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.State
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.enableSavedStateHandles
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.compose.ui.platform.LocalLifecycleOwner as PlatformLocalLifecycleManager

/**
 * Default implementation of [DialogLifecycleManager].
 * Implements same interfaces as fragments:
 * [LifecycleOwner], [ViewModelStoreOwner], [SavedStateRegistryOwner], [HasDefaultViewModelProviderFactory].
 * Also provides them to the corresponding composition locals.
 *
 * Notes on LifecycleOwner implementation:
 * * Parent Lifecycle events are propagated to the local LifecycleOwner.
 * * When a dialog enters a composition, ON_RESUME will be called immediately.
 * * When a dialog exists a composition, ON_DESTROYED will be called immediately.
 */
public class DefaultDialogLifecycleManager(
    override val key: String,
    private val defaultArguments: Bundle?
) : DialogLifecycleManager,
    LifecycleOwner,
    ViewModelStoreOwner,
    SavedStateRegistryOwner,
    HasDefaultViewModelProviderFactory {

    private var parentLifecycleState: State = State.INITIALIZED

    private var application: Application? = null
    private var savedState: Bundle? = null

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    private val store = ViewModelStore()
    private val defaultFactory by lazy(LazyThreadSafetyMode.NONE) {
        SavedStateViewModelFactory(requireNotNull(application), this)
    }
    private val defaultCreationExtras by lazy(LazyThreadSafetyMode.NONE) {
        MutableCreationExtras().also { extras ->
            extras[APPLICATION_KEY] = requireNotNull(application)
            extras[SAVED_STATE_REGISTRY_OWNER_KEY] = this
            extras[VIEW_MODEL_STORE_OWNER_KEY] = this
            if (defaultArguments != null) extras[DEFAULT_ARGS_KEY] = defaultArguments
        }
    }

    override val providedValues: Array<ProvidedValue<*>> by lazy(LazyThreadSafetyMode.NONE) {
        arrayOf(
            PlatformLocalLifecycleManager provides this,
            LocalLifecycleOwner provides this,
            LocalViewModelStoreOwner provides this,
            LocalSavedStateRegistryOwner provides this
        )
    }

    init {
        lifecycleRegistry.addObserver(object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                savedStateRegistryController.performSave(requireNotNull(savedState))
            }
            override fun onDestroy(owner: LifecycleOwner) {
                viewModelStore.clear()
            }
        })
    }

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val viewModelStore: ViewModelStore get() = store
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry
    override val defaultViewModelProviderFactory: ViewModelProvider.Factory get() = defaultFactory
    override val defaultViewModelCreationExtras: CreationExtras get() = defaultCreationExtras

    override fun initialize(application: Application, savedState: Bundle) {
        this.application = application
        this.savedState = savedState
    }

    override fun onRemoved() {
        if (lifecycle.currentState == State.INITIALIZED) return // Nothing to do, the screen wasn't even created.

        lifecycleRegistry.currentState = State.DESTROYED
    }

    override fun onLaunched(parentLifecycleState: State) {
        // Perform the restore just once and before we move up.
        if (lifecycle.currentState == State.INITIALIZED) {
            savedStateRegistryController.performAttach()
            enableSavedStateHandles()
            savedStateRegistryController.performRestore(savedState)
        }

        this.parentLifecycleState = parentLifecycleState
        lifecycleRegistry.currentState = parentLifecycleState
    }

    override fun onParentLifecycleStateChanged(state: State) {
        if (parentLifecycleState == state) return

        lifecycleRegistry.currentState = state
    }
}
