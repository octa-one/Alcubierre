package com.github.octaone.alcubierre.render

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import com.github.octaone.alcubierre.ComposeNavDriveOwner

@Composable
@NonRestartableComposable
public fun AlcubierreRender(
    navDriveOwner: ComposeNavDriveOwner
) {
    AlcubierreRenderImpl(
        navDriveOwner = navDriveOwner,
        animationType = NO_ANIMATION,
        addTransition = null,
        removeTransition = null,
        animationSpec = null
    )
}

@Composable
@NonRestartableComposable
public fun AlcubierreAnimatedRender(
    navDriveOwner: ComposeNavDriveOwner,
    transition: ScreenTransitionScope.() -> ContentTransform
) {
    AlcubierreRenderImpl(
        navDriveOwner = navDriveOwner,
        animationType = GENERIC_ANIMATION_SAME_ENTER_EXIT,
        addTransition = transition,
        removeTransition = transition,
        animationSpec = null
    )
}

@Composable
@NonRestartableComposable
public fun AlcubierreAnimatedRender(
    navDriveOwner: ComposeNavDriveOwner,
    addTransition: ScreenTransitionScope.() -> ContentTransform,
    removeTransition: ScreenTransitionScope.() -> ContentTransform
) {
    AlcubierreRenderImpl(
        navDriveOwner = navDriveOwner,
        animationType = GENERIC_ANIMATION,
        addTransition = addTransition,
        removeTransition = removeTransition,
        animationSpec = null
    )
}

@ExperimentalAnimationApi
@Composable
@NonRestartableComposable
public fun AlcubierreCrossfadeRender(
    navDriveOwner: ComposeNavDriveOwner,
    animationSpec: FiniteAnimationSpec<Float> = tween()
) {
    AlcubierreRenderImpl(
        navDriveOwner = navDriveOwner,
        animationType = CROSSFADE,
        addTransition = null,
        removeTransition = null,
        animationSpec = animationSpec
    )
}
