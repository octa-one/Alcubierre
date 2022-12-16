package com.github.octaone.alcubierre.host

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.github.octaone.alcubierre.LocalNavDriveProvider
import com.github.octaone.alcubierre.NavDrive

@Composable
fun Alcubierre(
    navDrive: NavDrive,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalNavDriveProvider provides navDrive,
        content = content
    )
}