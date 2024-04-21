package com.github.octaone.alcubierre

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.github.octaone.alcubierre.screen.ComposeDialog
import com.github.octaone.alcubierre.screen.ComposeScreen

public val LocalNavDrive: ProvidableCompositionLocal<NavDrive<ComposeScreen, ComposeDialog>> =
    staticCompositionLocalOf { error("No NavDrive provided") }
