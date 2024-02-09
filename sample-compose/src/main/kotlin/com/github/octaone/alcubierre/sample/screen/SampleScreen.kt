@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.octaone.alcubierre.sample.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.octaone.alcubierre.LocalNavDrive
import com.github.octaone.alcubierre.action.back
import com.github.octaone.alcubierre.action.backToRoot
import com.github.octaone.alcubierre.action.clearStack
import com.github.octaone.alcubierre.action.forward
import com.github.octaone.alcubierre.action.newStack
import com.github.octaone.alcubierre.action.replace
import com.github.octaone.alcubierre.action.replaceRoot
import com.github.octaone.alcubierre.action.showDialog
import com.github.octaone.alcubierre.sample.Counter
import com.github.octaone.alcubierre.sample.Tab
import com.github.octaone.alcubierre.screen.ComposeScreen
import com.github.octaone.alcubierre.screen.isShowing
import com.github.octaone.alcubierre.state.ComposeRootNavState
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class SampleScreen(
    val someId: Int
) : ComposeScreen() {

    @Composable
    override fun Content() {

        val navDrive = LocalNavDrive.current
        var text by rememberSaveable { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 56.dp, start = 16.dp, end = 16.dp)
        ) {

            Text(text = someId.toString(), fontWeight = FontWeight.Bold)
            Text(text = navDrive.state.toStackString())
            TextField(value = text, onValueChange = { text = it })
            Button(
                modifier = MaxWidthModifier,
                onClick = remember {{ navDrive.forward(SampleScreen(Counter.increment())) }}
            ) {
                Text(text = "Forward")
            }
            Button(
                modifier = MaxWidthModifier,
                onClick = remember {{ navDrive.replace(SampleScreen(Counter.increment())) }}
            ) {
                Text(text = "Replace")
            }
            Button(
                modifier = MaxWidthModifier,
                onClick = remember {{ navDrive.replaceRoot(SampleScreen(Counter.increment())) }}
            ) {
                Text(text = "New Root")
            }
            Button(
                modifier = MaxWidthModifier,
                onClick = remember {{ navDrive.back() }}
            ) {
                Text(text = "Back")
            }
            Button(
                modifier = MaxWidthModifier,
                onClick = remember {{ navDrive.backToRoot() }}
            ) {
                Text(text = "Back to root")
            }
            val context = LocalContext.current
            Button(
                modifier = MaxWidthModifier,
                onClick = remember {{
                    if (navDrive.state.stackStates.containsKey(Tab.TAB_2.id)) {
                        Toast.makeText(context, "Stack already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        navDrive.newStack(Tab.TAB_2.id, SampleScreen(Counter.increment()))
                    }
                }}
            ) {
                Text(text = "New Stack (Tab 2)")
            }
            Button(
                modifier = MaxWidthModifier,
                onClick = remember {{ navDrive.clearStack(Tab.TAB_2.id) }}
            ) {
                Text(text = "Clear Stack (Tab 2)")
            }
            Button(
                modifier = MaxWidthModifier,
                onClick = remember {{
                    navDrive.showDialog(SampleDialog(Counter.increment(), Random.nextInt(0, 10)))
                }}
            ) {
                Text(text = "Show dialog")
            }
            Button(
                modifier = MaxWidthModifier,
                onClick = remember {{
                    repeat(3) {
                        navDrive.showDialog(SampleDialog(Counter.increment(), Random.nextInt(0, 10)))
                    }
                }}
            ) {
                Text(text = "Show 3 dialogs")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private val MaxWidthModifier = Modifier.fillMaxWidth()

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
