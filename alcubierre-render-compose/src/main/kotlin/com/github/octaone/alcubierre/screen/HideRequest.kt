package com.github.octaone.alcubierre.screen

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.sync.Semaphore

@Stable
class HideRequest {

    val shouldBeHidden = mutableStateOf(false)

    private val hideSemaphore = Semaphore(1, 1)

    fun markHidden() {
        hideSemaphore.release()
    }

    internal suspend fun hideAndAwaitHidden() {
        shouldBeHidden.value = true
        hideSemaphore.acquire()
        shouldBeHidden.value = false
    }
}
