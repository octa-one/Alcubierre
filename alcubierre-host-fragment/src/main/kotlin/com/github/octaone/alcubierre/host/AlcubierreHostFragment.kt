package com.github.octaone.alcubierre.host

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.github.octaone.alcubierre.action.NavigationAction
import com.github.octaone.alcubierre.action.back
import com.github.octaone.alcubierre.render.modifier.TransactionModifier
import com.github.octaone.alcubierre.state.RootNavigationState
import androidx.lifecycle.Lifecycle
import com.github.octaone.alcubierre.Alcubierre
import com.github.octaone.alcubierre.AlcubierreHost
import com.github.octaone.alcubierre.fragment.host.R
import com.github.octaone.alcubierre.reduce.AlcubierreRootNavigationReducer
import com.github.octaone.alcubierre.render.AlcubierreRootNavigationRender

class AlcubierreHostFragment : Fragment(), AlcubierreHost {

    private val delegate = AlcubierreStandaloneHost()

    private var containerId: Int = -1
    private var restoredState: Bundle? = null

    override val state: RootNavigationState get() = delegate.state

    fun initialize(
        reducer: AlcubierreRootNavigationReducer,
        initialState: RootNavigationState,
        transactionModifier: TransactionModifier
    ) {
        check(lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED))

        val render = AlcubierreRootNavigationRender(
            containerId = containerId,
            classLoader = requireContext().classLoader,
            fragmentManager = childFragmentManager,
            transactionModifier = transactionModifier
        )

        delegate.initialize(
            initialState = initialState,
            reducer = reducer,
            render = render,
            savedState = restoredState
        )
        restoredState = null
    }

    override fun dispatch(action: NavigationAction) {
        delegate.dispatch(action)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this) { back() }
        parentFragmentManager.commit {
            setPrimaryNavigationFragment(this@AlcubierreHostFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        delegate.onResume()
    }

    override fun onPause() {
        super.onPause()
        delegate.onPause()
    }

    override fun saveState(outState: Bundle) {
        outState.putInt(KEY_CONTAINER_ID, containerId)
        delegate.saveState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        containerId = savedInstanceState?.getInt(KEY_CONTAINER_ID) ?: View.generateViewId()
        restoredState = savedInstanceState
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return FragmentContainerView(requireContext()).apply { id = containerId }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setTag(R.id.alcubierre_view_tag, this as Alcubierre)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireView().setTag(R.id.alcubierre_view_tag, null)
    }

    companion object {

        private const val KEY_CONTAINER_ID = "alcubierre_container"
    }
}
