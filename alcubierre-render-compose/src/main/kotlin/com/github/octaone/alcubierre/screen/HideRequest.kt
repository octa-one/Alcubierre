package com.github.octaone.alcubierre.screen

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.sync.Semaphore

@Stable
public class HideRequest {

    private val _shouldBeHidden = mutableStateOf(false)
    public val shouldBeHidden: State<Boolean> get() = _shouldBeHidden

    private val hideSemaphore = Semaphore(1, 1)

    public fun markHidden() {
        hideSemaphore.release()
    }

    internal suspend fun hideAndAwaitHidden() {
        _shouldBeHidden.value = true
        hideSemaphore.acquire()
        _shouldBeHidden.value = false
    }
}
