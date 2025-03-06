package com.github.octaone.alcubierre

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.github.octaone.alcubierre.render.AlcubierreAnimatedRender

/**
 * Composition local for [AnimatedContentScope].
 * [AlcubierreAnimatedRender] provides [AnimatedContentScope] value.
 *
 * Can be used for [shared element transitions](https://developer.android.com/develop/ui/compose/animation/shared-elements).
 */
public val LocalRenderAnimatedContentScope: ProvidableCompositionLocal<AnimatedContentScope> =
    staticCompositionLocalOf { error("No AnimatedContentScope provided") }
