package com.github.octaone.alcubierre

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.github.octaone.alcubierre.render.AlcubierreRender

/**
 * Composition local for [NavDrive].
 * [AlcubierreRender] provides [NavDrive] value.
 */
public val LocalNavDrive: ProvidableCompositionLocal<ComposeNavDrive> =
    staticCompositionLocalOf { error("No NavDrive provided") }
