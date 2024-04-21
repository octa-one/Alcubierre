package com.github.octaone.alcubierre.lifecycle

import android.app.Application
import android.os.Bundle
import androidx.compose.runtime.ProvidedValue
import androidx.lifecycle.Lifecycle

public interface LifecycleManager {

    public val key: String

    public val providedValues: Array<ProvidedValue<*>>

    public fun initialize(application: Application, savedState: Bundle)

    public fun onLaunched(parentLifecycleState: Lifecycle.State)

    public fun onRemoved()

    public fun onParentLifecycleStateChanged(state: Lifecycle.State)
}

public interface ScreenLifecycleManager : LifecycleManager {

    public fun onStacked()

    public fun onEnterTransitionFinished()

    public fun onExitTransitionFinished()
}

public typealias DialogLifecycleManager = LifecycleManager
