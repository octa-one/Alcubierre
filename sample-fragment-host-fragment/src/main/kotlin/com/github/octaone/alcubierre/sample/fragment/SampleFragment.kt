package com.github.octaone.alcubierre.sample.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.octaone.alcubierre.action.back
import com.github.octaone.alcubierre.action.backToRoot
import com.github.octaone.alcubierre.action.clearStack
import com.github.octaone.alcubierre.action.forward
import com.github.octaone.alcubierre.action.newStack
import com.github.octaone.alcubierre.action.replace
import com.github.octaone.alcubierre.action.replaceRoot
import com.github.octaone.alcubierre.action.showDialog
import com.github.octaone.alcubierre.findNavDrive
import com.github.octaone.alcubierre.sample.Counter
import com.github.octaone.alcubierre.sample.R
import com.github.octaone.alcubierre.sample.databinding.Fmt0Binding
import com.github.octaone.alcubierre.sample.screen.SampleDialog
import com.github.octaone.alcubierre.sample.screen.SampleScreen
import com.github.octaone.alcubierre.screen.isShowing
import com.github.octaone.alcubierre.screen.screenData
import com.github.octaone.alcubierre.state.RootNavState
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

    private fun RootNavState.toStackString() =
        buildString {
            appendLine("Stacks (* - current screen):")
            stackStates.forEach { (id, stack) ->
                append(
                    when (id) {
                        R.id.stack_0 -> "0"
                        R.id.stack_1 -> "1"
                        else -> "2"
                    }
                )
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
}