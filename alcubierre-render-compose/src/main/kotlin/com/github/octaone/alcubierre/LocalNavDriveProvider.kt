package com.github.octaone.alcubierre

import androidx.compose.runtime.staticCompositionLocalOf

val LocalNavDrive = staticCompositionLocalOf<ComposeNavDrive> { error("No NavDrive provided") }
