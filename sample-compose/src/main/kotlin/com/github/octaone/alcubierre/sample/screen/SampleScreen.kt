@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.octaone.alcubierre.sample.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleEventObserver
import com.github.octaone.alcubierre.LocalNavDrive
import com.github.octaone.alcubierre.action.back
import com.github.octaone.alcubierre.action.forward
import com.github.octaone.alcubierre.sample.Counter
import com.github.octaone.alcubierre.screen.ComposeScreen
import com.github.octaone.alcubierre.screen.isShowing
import com.github.octaone.alcubierre.state.ComposeRootNavState
import kotlinx.parcelize.Parcelize

@Parcelize
data class SampleScreen(
    val someId: Int
) : ComposeScreen() {

    @Composable
    override fun Content() {

        val navDrive = LocalNavDrive.current
        var text by rememberSaveable { mutableStateOf("") }

        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                Log.w("LIFECYCLE", "${screenId.takeLast(4)} > SCREEN $someId ${event.name}")
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                Log.w("LIFECYCLE", "${screenId.takeLast(4)} > SCREEN $someId onDispose")
                owner.lifecycle.removeObserver(observer)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp, start = 16.dp, end = 16.dp)
        ) {

            BackHandler { navDrive.back() }

            Text(text = someId.toString(), fontWeight = FontWeight.Bold)
            Text(text = navDrive.state.toStackString())
            TextField(value = text, onValueChange = { text = it })
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navDrive.forward(SampleScreen(Counter.increment())) }
            ) {
                Text(text = "Forward")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navDrive.back() }
            ) {
                Text(text = "Back")
            }
            /*
            TODO
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navDrive.showDialog(SampleDialog(123)) }
            ) {
                Text(text = "Show dialog")
            }
            */
        }
    }
}

private fun ComposeRootNavState.toStackString() =
    buildString {
        appendLine("Stacks (* - current screen):")
        stackStates.forEach { (id, stack) ->
            append(id)
            append(" : ")
            stack.chain.joinTo(this) { it.screenId.takeLast(4) }
            if (id == currentStackId) append("*")
            appendLine()
        }
        appendLine("Dialogs queue (* - visible dialog):")
        dialogState.queue.joinTo(this) {
            val shortId = it.dialogId.takeLast(4)
            if (it.isShowing) "$shortId(p=${it.priority})*" else "$shortId(p=${it.priority})"
        }
    }
