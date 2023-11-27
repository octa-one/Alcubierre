package com.github.octaone.alcubierre.lifecycle

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.enableSavedStateHandles
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController

class ScreenLifecycleManagerImpl(
    override val id: String,
    private val defaultArguments: Bundle?
) : ScreenLifecycleManager {

    override var parentLifecycleState: Lifecycle.State = Lifecycle.State.INITIALIZED
        set(state) {
            if (field == state) return
            field = state
            updateLifecycleState(state)
        }

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    private val store = ViewModelStore()

    private var app: Application? = null

    private val defaultFactory by lazy { SavedStateViewModelFactory(app, this) }

    override fun setApplication(app: Application?) {
        this.app = app
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val viewModelStore: ViewModelStore
        get() = store

    override val savedStateRegistry: SavedStateRegistry =
        savedStateRegistryController.savedStateRegistry

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = defaultFactory

    override val defaultViewModelCreationExtras: CreationExtras
        get() {
            val extras = MutableCreationExtras()
            app?.let { application -> extras[APPLICATION_KEY] = application }
            extras[SAVED_STATE_REGISTRY_OWNER_KEY] = this
            extras[VIEW_MODEL_STORE_OWNER_KEY] = this
            defaultArguments?.let { args -> extras[DEFAULT_ARGS_KEY] = args }
            return extras
        }

    override fun handleLifecycleEvent(event: Lifecycle.Event) {
        updateLifecycleState(event.targetState)
    }

    override fun updateLifecycleState(state: Lifecycle.State) {
        val actualState = state.coerceAtMost(parentLifecycleState)
        lifecycleRegistry.currentState = actualState
    }

    override fun performRestore(savedState: Bundle?) {
        // Perform the restore just once and before we move up the Lifecycle
        if (lifecycle.currentState == Lifecycle.State.INITIALIZED) {
            savedStateRegistryController.performAttach()
            enableSavedStateHandles()
            savedStateRegistryController.performRestore(savedState)
        }
    }

    override fun performSave(outState: Bundle) {
        savedStateRegistryController.performSave(outState)
    }
}

@Composable
internal fun ScreenLifecycleManager.LifecycleHandler(parentLifecycle: Lifecycle) {
    val savedState = rememberSaveable(key = "$id:bundle") { Bundle() }
    val context = LocalContext.current
    setApplication(context.applicationContext as Application)
    performRestore(savedState)
    parentLifecycleState = parentLifecycle.currentState
    updateLifecycleState(parentLifecycle.currentState) // force update state after popping backstack
    DisposableEffect(Unit) {
        val parentObserver = LifecycleEventObserver { owner, event ->
            if (event == Lifecycle.Event.ON_DESTROY &&
                context.findActivity()?.isChangingConfigurations == true) {
                /**
                 * Instance of the screen isn't recreated during config changes so skip this event
                 * to avoid crash while accessing to ViewModel with SavedStateHandle, because after
                 * ON_DESTROY, [androidx.lifecycle.SavedStateHandleController] is marked as not
                 * attached and next call of [registerSavedStateProvider] after recreating Activity
                 * on the same instance causing the crash.
                 */
                return@LifecycleEventObserver
            }

            parentLifecycleState = owner.lifecycle.currentState
        }
        val observer = LifecycleEventObserver { owner, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    performSave(savedState)
                }
                Lifecycle.Event.ON_DESTROY -> {
                    viewModelStore.clear()
                }
                else -> Unit
            }
        }

        parentLifecycle.addObserver(parentObserver)
        lifecycle.addObserver(observer)

        onDispose {
            parentLifecycle.removeObserver(parentObserver)
            lifecycle.removeObserver(observer)
        }
    }
}

private tailrec fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
