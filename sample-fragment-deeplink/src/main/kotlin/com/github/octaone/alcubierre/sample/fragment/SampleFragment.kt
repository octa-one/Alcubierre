package com.github.octaone.alcubierre.sample.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
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
import com.github.octaone.alcubierre.screen.screenData
import com.github.octaone.alcubierre.state.RootNavState

class SampleFragment : Fragment(R.layout.fmt_0) {

    private val navDrive: NavDrive get() = SampleApplication.from(requireContext()).navDrive
    private val screen: SampleScreen by screenData()

    private lateinit var binding: Fmt0Binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = Fmt0Binding.bind(view)

        with(binding) {
            text.text = "${screen.someId}, uri: ${screen.deeplinkUri}"
            showStackState()

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

        parentFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentLifecycleCallbacks() {

            override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                super.onFragmentAttached(fm, f, context)
                showStackState()
            }

            override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                super.onFragmentDetached(fm, f)
                showStackState()
            }

        }, false)
    }

    private fun showStackState() {
        binding.textState.text = navDrive.state.toStackString()
    }

    private fun RootNavState.toStackString() =
        buildString {
            appendLine("Stacks (* - current screen):")
            stackStates.forEach { (id, stack) ->
                append(id)
                append(" : ")
                stack.chain.joinTo(this) { it.screenId.substringAfterLast('.') }
                if (id == currentStackId) append(" *")
                appendLine()
            }
            append("Dialog: ")
            appendLine(currentDialog?.dialogId?.substringAfterLast('.'))
        }
}
