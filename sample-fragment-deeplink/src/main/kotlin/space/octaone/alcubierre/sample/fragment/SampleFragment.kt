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
import androidx.fragment.app.Fragment
import space.octaone.alcubierre.FragmentNavDrive
import space.octaone.alcubierre.action.back
import space.octaone.alcubierre.action.forward
import space.octaone.alcubierre.action.replaceRoot
import space.octaone.alcubierre.sample.Counter
import space.octaone.alcubierre.sample.R
import space.octaone.alcubierre.sample.SampleApplication
import space.octaone.alcubierre.sample.databinding.Fmt0Binding
import space.octaone.alcubierre.sample.screen.SampleScreen
import space.octaone.alcubierre.screen.deeplinkUri
import space.octaone.alcubierre.screen.screenData
import space.octaone.alcubierre.state.FragmentRootNavState

class SampleFragment : Fragment(R.layout.fmt_0) {

    private val navDrive: FragmentNavDrive
        get() = SampleApplication.Companion.from(requireContext()).navDrive
    private val screen: SampleScreen by screenData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = Fmt0Binding.bind(view)

        with(binding) {
            text.text = "${screen.someId}, uri: ${screen.deeplinkUri}"
            binding.textState.text = navDrive.state.toStackString()

            btnBack.setOnClickListener {
                navDrive.back()
            }

            btnShowDialog.setOnClickListener {
                navDrive.forward("myapp://dialog")
            }

            btnForward.setOnClickListener {
                navDrive.forward("myapp://sample?id=${Counter.increment()}")
            }

            btnForwardConditional.setOnClickListener {
                navDrive.forward("myapp://sample/condition?id=${Counter.increment()}")
            }

            btnNewRoot.setOnClickListener {
                navDrive.replaceRoot(SampleScreen(Counter.increment()))
            }
        }

        parentFragmentManager.setFragmentResultListener("state", this) { _, _ ->
            view.post { binding.textState.text = navDrive.state.toStackString() }
        }
    }

    private fun FragmentRootNavState.toStackString() =
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
}
