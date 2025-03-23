/*
 *  Copyright 2022-2025. Alcubierre Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(ExperimentalMaterial3Api::class)

package space.octaone.alcubierre.sample.screen

import android.os.Bundle
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import kotlinx.parcelize.Parcelize
import space.octaone.alcubierre.LocalNavDrive
import space.octaone.alcubierre.action.back
import space.octaone.alcubierre.action.backToRoot
import space.octaone.alcubierre.action.clearStack
import space.octaone.alcubierre.action.forward
import space.octaone.alcubierre.action.newStack
import space.octaone.alcubierre.action.replace
import space.octaone.alcubierre.action.replaceRoot
import space.octaone.alcubierre.action.showDialog
import space.octaone.alcubierre.hilt.hiltViewModel
import space.octaone.alcubierre.sample.Counter
import space.octaone.alcubierre.sample.Tab
import space.octaone.alcubierre.sample.viewmodel.SampleViewModel
import space.octaone.alcubierre.screen.ComposeScreen
import space.octaone.alcubierre.state.ComposeRootNavState
import kotlin.random.Random

@Parcelize
class SampleScreen(
    val someId: Int
) : ComposeScreen() {

    override fun getSavedStateDefaultArguments(): Bundle =
        bundleOf("SOME_ID" to someId)

    @Composable
    override fun Content() {

        val viewModel: SampleViewModel = hiltViewModel { factory: SampleViewModel.Factory ->
            factory.create(someId)
        }

        val navDrive = LocalNavDrive.current
        var text by rememberSaveable { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 56.dp, start = 16.dp, end = 16.dp)
        ) {

            Text(text = viewModel.someId.toString(), fontWeight = FontWeight.Bold)
            Text(text = navDrive.state.toStackString())
            TextField(value = text, onValueChange = { text = it })
            Button(
                modifier = MaxWidthModifier,
                onClick = { navDrive.forward(SampleScreen(Counter.increment())) }
            ) {
                Text(text = "Forward")
            }
            Button(
                modifier = MaxWidthModifier,
                onClick = { navDrive.replace(SampleScreen(Counter.increment())) }
            ) {
                Text(text = "Replace")
            }
            Button(
                modifier = MaxWidthModifier,
                onClick = { navDrive.replaceRoot(SampleScreen(Counter.increment())) }
            ) {
                Text(text = "New Root")
            }
            Button(
                modifier = MaxWidthModifier,
                onClick = { navDrive.back() }
            ) {
                Text(text = "Back")
            }
            Button(
                modifier = MaxWidthModifier,
                onClick = { navDrive.backToRoot() }
            ) {
                Text(text = "Back to root")
            }
            val context = LocalContext.current
            Button(
                modifier = MaxWidthModifier,
                onClick = {
                    if (navDrive.state.stackStates.containsKey(Tab.TAB_2.id)) {
                        Toast.makeText(context, "Stack already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        navDrive.newStack(Tab.TAB_2.id, SampleScreen(Counter.increment()))
                    }
                }
            ) {
                Text(text = "New Stack (Tab 2)")
            }
            Button(
                modifier = MaxWidthModifier,
                onClick = { navDrive.clearStack(Tab.TAB_2.id) }
            ) {
                Text(text = "Clear Stack (Tab 2)")
            }
            Button(
                modifier = MaxWidthModifier,
                onClick = {
                    navDrive.showDialog(SampleDialog(Counter.increment(), Random.nextInt(0, 10)))
                }
            ) {
                Text(text = "Show dialog")
            }
            Button(
                modifier = MaxWidthModifier,
                onClick = {
                    repeat(3) {
                        navDrive.showDialog(SampleDialog(Counter.increment(), Random.nextInt(0, 10)))
                    }
                }
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
        stackStates.forEach { (id, stackState) ->
            append(id)
            append(" : ")
            stackState.stack.joinTo(this) { it.screenId.takeLast(4) }
            if (id == currentStackId) append("*")
            appendLine()
        }
        appendLine("Dialogs queue (* - visible dialog):")
        dialogState.queue.joinTo(this) {
            val shortId = it.dialogId.takeLast(4)
            if (it.isShowing) "$shortId(p=${it.priority})*" else "$shortId(p=${it.priority})"
        }
    }
