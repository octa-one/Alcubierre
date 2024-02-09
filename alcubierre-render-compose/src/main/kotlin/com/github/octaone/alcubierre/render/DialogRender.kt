package com.github.octaone.alcubierre.render

import androidx.compose.runtime.Composable
import com.github.octaone.alcubierre.screen.ComposeDialog

fun interface DialogRender {

    @Composable
    fun Content(onDismissRequest: () -> Unit, dialog: ComposeDialog)
}
