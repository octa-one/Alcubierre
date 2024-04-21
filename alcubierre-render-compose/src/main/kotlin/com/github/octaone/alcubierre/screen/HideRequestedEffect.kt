package com.github.octaone.alcubierre.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

@Immutable
public fun interface ImmutableSuspendFunction {

    public suspend operator fun invoke()
}

@Composable
public fun HideRequestedEffect(
    hideRequest: HideRequest,
    hideAction: ImmutableSuspendFunction
) {
    val shouldBeHidden by hideRequest.shouldBeHidden
    LaunchedEffect(shouldBeHidden) {
        if (shouldBeHidden) {
            hideAction()
            hideRequest.markHidden()
        }
    }
}
