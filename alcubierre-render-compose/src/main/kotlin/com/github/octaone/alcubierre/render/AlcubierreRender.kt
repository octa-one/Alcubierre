package com.github.octaone.alcubierre.render

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import com.github.octaone.alcubierre.ComposeNavDriveOwner
import com.github.octaone.alcubierre.state.ComposeRootNavState
import com.github.octaone.alcubierre.state.RootNavState

/**
 * Render for [ComposeRootNavState].
 * This implementation does not use animated transitions, screens switch instantly.
 *
 * @see AlcubierreRenderImpl
 */
@Composable
@NonRestartableComposable
public fun AlcubierreRender(
    navDriveOwner: ComposeNavDriveOwner
) {
    AlcubierreRenderImplWithProvider(
        navDriveOwner = navDriveOwner,
        animationType = NO_ANIMATION,
        addTransition = null,
        removeTransition = null,
        animationSpec = null
    )
}

/**
 * Render for [ComposeRootNavState].
 * This implementation does use animated transitions.
 * Only one [transition] is required, which is used for any screen change.
 * To specify different transitions for add and remove actions, use an overload with two transitions.
 *
 * @see AlcubierreRenderImpl
 */
@Composable
@NonRestartableComposable
public fun AlcubierreAnimatedRender(
    navDriveOwner: ComposeNavDriveOwner,
    transition: ScreenTransitionScope.() -> ContentTransform
) {
    AlcubierreRenderImplWithProvider(
        navDriveOwner = navDriveOwner,
        animationType = GENERIC_ANIMATION_SAME_ENTER_EXIT,
        addTransition = transition,
        removeTransition = transition,
        animationSpec = null
    )
}

/**
 * Render for [ComposeRootNavState].
 * This implementation does use animated transitions.
 * [addTransition] is used when the screen is added to [RootNavState].
 * [removeTransition] is used when the screen is removed from [RootNavState].
 *
 * @see AlcubierreRenderImpl
 */
@Composable
@NonRestartableComposable
public fun AlcubierreAnimatedRender(
    navDriveOwner: ComposeNavDriveOwner,
    addTransition: ScreenTransitionScope.() -> ContentTransform,
    removeTransition: ScreenTransitionScope.() -> ContentTransform
) {
    AlcubierreRenderImplWithProvider(
        navDriveOwner = navDriveOwner,
        animationType = GENERIC_ANIMATION,
        addTransition = addTransition,
        removeTransition = removeTransition,
        animationSpec = null
    )
}

/**
 * Render for [ComposeRootNavState].
 * This implementation does use crossfaded transitions.
 * It is based on the [Crossfade] transition.
 * Its implementation is simpler compared to the generic [AnimatedContent] used in [AlcubierreAnimatedRender].
 *
 * @see AlcubierreRenderImpl
 */
@ExperimentalAnimationApi
@Composable
@NonRestartableComposable
public fun AlcubierreCrossfadeRender(
    navDriveOwner: ComposeNavDriveOwner,
    animationSpec: FiniteAnimationSpec<Float> = tween()
) {
    AlcubierreRenderImplWithProvider(
        navDriveOwner = navDriveOwner,
        animationType = CROSSFADE,
        addTransition = null,
        removeTransition = null,
        animationSpec = animationSpec
    )
}
