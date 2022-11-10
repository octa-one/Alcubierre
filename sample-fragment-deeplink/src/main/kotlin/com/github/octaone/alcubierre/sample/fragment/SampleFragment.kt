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

        binding.btnForward.setOnClickListener {
            navDrive.forward("myapp://sample?id=${Counter.increment()}")
        }
        binding.btnNewRoot.setOnClickListener {
            navDrive.replaceRoot(SampleScreen(Counter.increment()))
        }
    }

    private fun RootNavState.toStackString() =
        buildString {
            appendLine("Stacks (* - current screen):")
            stacks.forEach { (id, stack) ->
                append(id)
                append(" : ")
                stack.chain.joinTo(this) { it.screenId.substringAfterLast('.') }
                if (id == currentStackId) append(" *")
                appendLine()
            }
            append("Dialog: ")
            appendLine(dialog?.dialogId?.substringAfterLast('.'))
        }
}
