package com.github.octaone.alcubierre.sample.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.action.back
import com.github.octaone.alcubierre.action.forward
import com.github.octaone.alcubierre.action.replaceRoot
import com.github.octaone.alcubierre.sample.Counter
import com.github.octaone.alcubierre.sample.R
import com.github.octaone.alcubierre.sample.SampleApplication
import com.github.octaone.alcubierre.sample.databinding.Fmt0Binding
import com.github.octaone.alcubierre.sample.screen.SampleScreen
import com.github.octaone.alcubierre.screen.deeplinkUri
import com.github.octaone.alcubierre.screen.isShowing
import com.github.octaone.alcubierre.screen.screenData
import com.github.octaone.alcubierre.state.RootNavState

class SampleFragment : Fragment(R.layout.fmt_0) {

    private val navDrive: NavDrive get() = SampleApplication.from(requireContext()).navDrive
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
            btnNewRoot.setOnClickListener {
                navDrive.replaceRoot(SampleScreen(Counter.increment()))
            }
        }

        parentFragmentManager.setFragmentResultListener("state", this) { _, _ ->
            view.post { binding.textState.text = navDrive.state.toStackString() }
        }
    }

    private fun RootNavState.toStackString() =
        buildString {
            appendLine("Stacks (* - current screen):")
            stackStates.forEach { (id, stack) ->
                append(id)
                append(" : ")
                stack.chain.joinTo(this) { it.screenId.substringAfterLast('.') }
                if (id == currentStackId) append("*")
                appendLine()
            }
            appendLine("Dialogs queue (* - visible dialog):")
            dialogState.queue.joinTo(this) {
                val shortId = it.dialogId.substringAfterLast('.')
                if (it.isShowing) "$shortId(p=${it.priority})*" else "$shortId(p=${it.priority})"
            }
        }
}
