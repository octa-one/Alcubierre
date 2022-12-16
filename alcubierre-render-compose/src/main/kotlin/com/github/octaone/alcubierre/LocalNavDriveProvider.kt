package com.github.octaone.alcubierre

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.github.octaone.alcubierre.screen.ComposeScreen

val LocalNavDriveProvider = staticCompositionLocalOf<NavDrive> { error("No NavDrive provided") }

@Suppress("unused")
val ComposeScreen.navDrive: NavDrive @Composable get() = LocalNavDriveProvider.current
