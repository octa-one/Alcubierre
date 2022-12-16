package com.github.octaone.alcubierre.sample.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.action.back
import com.github.octaone.alcubierre.action.backToRoot
import com.github.octaone.alcubierre.action.clearStack
import com.github.octaone.alcubierre.action.forward
import com.github.octaone.alcubierre.action.newStack
import com.github.octaone.alcubierre.action.replace
import com.github.octaone.alcubierre.action.replaceRoot
import com.github.octaone.alcubierre.action.showDialog
import com.github.octaone.alcubierre.sample.Counter
import com.github.octaone.alcubierre.sample.R
import com.github.octaone.alcubierre.sample.SampleApplication
import com.github.octaone.alcubierre.sample.databinding.Fmt0Binding
import com.github.octaone.alcubierre.sample.screen.SampleDialog
import com.github.octaone.alcubierre.sample.screen.SampleScreen
import com.github.octaone.alcubierre.screen.screenData
import com.github.octaone.alcubierre.state.RootNavState

class SampleFragment : Fragment(R.layout.fmt_0) {

    private val navDrive: NavDrive get() = SampleApplication.from(requireContext()).navDrive
    private val screen: SampleScreen by screenData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = Fmt0Binding.bind(view)

        binding.text.text = screen.someId.toString()
        binding.textState.text = navDrive.state.toStackString()

        binding.btnBack.setOnClickListener {
            navDrive.back()
        }

        binding.btnBackToRoot.setOnClickListener {
            navDrive.backToRoot()
        }

        binding.btnForward.setOnClickListener {
            navDrive.forward(SampleScreen(Counter.increment()))
        }

        binding.btnReplace.setOnClickListener {
            navDrive.replace(SampleScreen(Counter.increment()))
        }

        binding.btnNewRoot.setOnClickListener {
            navDrive.replaceRoot(SampleScreen(Counter.increment()))
        }

        binding.btnNewStack.setOnClickListener {
            if (navDrive.state.stackStates.containsKey(R.id.stack_2)) {
                Toast.makeText(requireContext(), "Stack already exists", Toast.LENGTH_SHORT).show()
            } else {
                navDrive.newStack(R.id.stack_2, SampleScreen(Counter.increment()))
                binding.textState.text = navDrive.state.toStackString()
            }
        }

        binding.btnClearStack.setOnClickListener {
            navDrive.clearStack(R.id.stack_2)
            binding.textState.text = navDrive.state.toStackString()
        }

        binding.btnShowDialog.setOnClickListener {
            navDrive.showDialog(SampleDialog(Counter.increment()))
            binding.textState.text = navDrive.state.toStackString()
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
                stack.chain.joinTo(this) { it.screenId.substringAfterLast('.') }
                if (id == currentStackId) append(" *")
                appendLine()
            }
            append("Dialog: ")
            appendLine(currentDialog?.dialogId?.substringAfterLast('.'))
        }
}