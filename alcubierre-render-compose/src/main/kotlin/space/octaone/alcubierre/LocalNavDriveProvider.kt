package space.octaone.alcubierre

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import space.octaone.alcubierre.render.AlcubierreRender

/**
 * Composition local for [space.octaone.alcubierre.base.NavDrive].
 * [AlcubierreRender] provides [space.octaone.alcubierre.base.NavDrive] value.
 */
public val LocalNavDrive: ProvidableCompositionLocal<ComposeNavDrive> =
    staticCompositionLocalOf { error("No NavDrive provided") }
