package com.github.octaone.alcubierre.sample.screen

import androidx.compose.runtime.Composable
import com.github.octaone.alcubierre.screen.ComposeDialog
import kotlinx.parcelize.Parcelize

@Parcelize
data class SampleDialog(
    val someId: Int
) : ComposeDialog() {

    @Composable
    override fun Content() {
        // TODO
    }
}