package com.github.octaone.alcubierre.lifecycle

import android.app.Application
import android.os.Bundle
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
import androidx.lifecycle.enableSavedStateHandles
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner

class ScreenLifecycleManager(
    override val key: String,
    private val defaultArguments: Bundle?
) : LifecycleManager,
    ScreenLifecycleOwner,
    ViewModelStoreOwner,
    SavedStateRegistryOwner,
    HasDefaultViewModelProviderFactory {

    private var parentLifecycleState: State = State.INITIALIZED

    override var isFinishing: Boolean = false; private set
    private var isResuming: Boolean = false

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

    fun onStacked() {
        // The screen moved to the backstack.
        isResuming = false
        lifecycleRegistry.currentState = State.CREATED
    }

    fun onRemoved() {
        if (lifecycle.currentState == State.INITIALIZED) return // Nothing to do, the screen wasn't even created.

        // Do not move to the DESTROYED state until the transition is completed.
        isFinishing = true
        lifecycleRegistry.currentState = State.CREATED
    }

    override fun onLaunched(parentLifecycleState: State) {
        isFinishing = false

        // Perform the restore just once and before we move up.
        if (lifecycle.currentState == State.INITIALIZED) {
            savedStateRegistryController.performAttach()
            enableSavedStateHandles()
            savedStateRegistryController.performRestore(savedState)
        }

        this.parentLifecycleState = parentLifecycleState
        if (parentLifecycleState == State.RESUMED) {
            // Do not move to the RESUMED state until the transition is completed.
            isResuming = true
            lifecycleRegistry.currentState = State.STARTED
        } else {
            lifecycleRegistry.currentState = parentLifecycleState
        }
    }

    override fun onParentLifecycleStateChanged(state: State) {
        if (parentLifecycleState == state) return

        lifecycleRegistry.currentState = state
    }

    fun onEnterTransitionFinished() {
        if (!isResuming) return
        // Trying to move to the RESUMED state, but not higher than the parent state.
        lifecycleRegistry.currentState = minOf(State.RESUMED, parentLifecycleState)
    }

    fun onExitTransitionFinished() {
        if (!isFinishing) return
        lifecycleRegistry.currentState = State.DESTROYED
        application = null
        savedState = null
    }
}
