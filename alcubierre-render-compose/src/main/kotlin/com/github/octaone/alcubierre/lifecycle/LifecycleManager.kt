package com.github.octaone.alcubierre.lifecycle

import android.app.Application
import android.os.Bundle
import androidx.compose.runtime.ProvidedValue
import androidx.lifecycle.Lifecycle

interface LifecycleManager {

    val key: String

    val providedValues: Array<ProvidedValue<*>>

    fun initialize(application: Application, savedState: Bundle)

    fun onLaunched(parentLifecycleState: Lifecycle.State)

    fun onRemoved()

    fun onParentLifecycleStateChanged(state: Lifecycle.State)
}

interface ScreenLifecycleManager : LifecycleManager {

    fun onStacked()

    fun onEnterTransitionFinished()

    fun onExitTransitionFinished()
}

typealias DialogLifecycleManager = LifecycleManager
