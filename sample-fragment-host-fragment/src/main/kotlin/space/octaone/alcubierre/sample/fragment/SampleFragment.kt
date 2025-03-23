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

package space.octaone.alcubierre.sample.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import space.octaone.alcubierre.action.back
import space.octaone.alcubierre.action.backToRoot
import space.octaone.alcubierre.action.clearStack
import space.octaone.alcubierre.action.forward
import space.octaone.alcubierre.action.newStack
import space.octaone.alcubierre.action.replace
import space.octaone.alcubierre.action.replaceRoot
import space.octaone.alcubierre.action.showDialog
import space.octaone.alcubierre.findNavDrive
import space.octaone.alcubierre.sample.Counter
import space.octaone.alcubierre.sample.R
import space.octaone.alcubierre.sample.databinding.Fmt0Binding
import space.octaone.alcubierre.sample.screen.SampleDialog
import space.octaone.alcubierre.sample.screen.SampleScreen
import space.octaone.alcubierre.screen.screenData
import space.octaone.alcubierre.state.FragmentRootNavState
import kotlin.random.Random

class SampleFragment : Fragment(R.layout.fmt_0) {

    private val screen: SampleScreen by screenData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = Fmt0Binding.bind(view)

        binding.text.text = screen.someId.toString()
        binding.textState.text = findNavDrive().state.toStackString()

        binding.btnBack.setOnClickListener {
            findNavDrive().back()
        }

        binding.btnBackToRoot.setOnClickListener {
            findNavDrive().backToRoot()
        }

        binding.btnForward.setOnClickListener {
            findNavDrive().forward(SampleScreen(Counter.increment()))
        }

        binding.btnReplace.setOnClickListener {
            findNavDrive().replace(SampleScreen(Counter.increment()))
        }

        binding.btnNewRoot.setOnClickListener {
            findNavDrive().replaceRoot(SampleScreen(Counter.increment()))
        }

        binding.btnNewStack.setOnClickListener {
            val navDrive = findNavDrive()
            if (navDrive.state.stackStates.containsKey(R.id.stack_2)) {
                Toast.makeText(requireContext(), "Stack already exists", Toast.LENGTH_SHORT).show()
            } else {
                findNavDrive().newStack(R.id.stack_2, SampleScreen(Counter.increment()))
            }
        }

        binding.btnClearStack.setOnClickListener {
            findNavDrive().clearStack(R.id.stack_2)
        }

        binding.btnShowDialog.setOnClickListener {
            findNavDrive().showDialog(SampleDialog(Counter.increment(), Random.nextInt(0, 10)))
        }

        binding.btnShowMultipleDialog.setOnClickListener {
            repeat(3) {
                findNavDrive().showDialog(SampleDialog(Counter.increment(), Random.nextInt(0, 10)))
            }
        }

        parentFragmentManager.setFragmentResultListener("state", this) { _, _ ->
            view.post { binding.textState.text = findNavDrive().state.toStackString() }
        }
    }

    private fun FragmentRootNavState.toStackString() =
        buildString {
            appendLine("Stacks (* - current screen):")
            stackStates.forEach { (id, stackState) ->
                append(
                    when (id) {
                        R.id.stack_0 -> "0"
                        R.id.stack_1 -> "1"
                        else -> "2"
                    }
                )
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
}
