/*
 *  Copyright 2022-2025. Alcubierre Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.octaone.alcubierre.render

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import space.octaone.alcubierre.ComposeNavDriveOwner
import space.octaone.alcubierre.base.state.RootNavState
import space.octaone.alcubierre.state.ComposeRootNavState

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
