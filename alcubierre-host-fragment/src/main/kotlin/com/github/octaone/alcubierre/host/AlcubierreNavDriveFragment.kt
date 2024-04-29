@file:OptIn(AlcubierreInternalApi::class)

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
import androidx.lifecycle.Lifecycle
import com.github.octaone.alcubierre.FragmentNavDriveOwner
import com.github.octaone.alcubierre.NavDriveOwner
import com.github.octaone.alcubierre.action.NavAction
import com.github.octaone.alcubierre.action.back
import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi
import com.github.octaone.alcubierre.owner.AlcubierreNavDriveOwner
import com.github.octaone.alcubierre.reduce.NavReducer
import com.github.octaone.alcubierre.render.AlcubierreRootNavRender
import com.github.octaone.alcubierre.render.modifier.EmptyModifier
import com.github.octaone.alcubierre.render.modifier.FragmentTransactionModifier
import com.github.octaone.alcubierre.render.renderFrom
import com.github.octaone.alcubierre.screen.FragmentDialog
import com.github.octaone.alcubierre.screen.FragmentScreen
import com.github.octaone.alcubierre.state.AnyRootNavState
import com.github.octaone.alcubierre.state.FragmentRootNavState
import com.github.octaone.alcubierre.util.getAndCast
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KClass

/**
 * [NavDriveOwner] implementation base on [Fragment].
 * Contains all the logic for working with states, so you don't need to save/restore states manually.
 * Intended for use in [FragmentContainerView]:
 * ```
 * <androidx.fragment.app.FragmentContainerView
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent"
 *     android:id="@+id/nav_host_fragment"
 *     android:name="com.github.octaone.alcubierre.host.AlcubierreNavDriveFragment" />
 * ```
 *
 * Can be configured with a [FragmentTransactionModifier] passed to extras at [initialization][initialize].
 */
public class AlcubierreNavDriveFragment : Fragment(), FragmentNavDriveOwner {

    private val delegate = AlcubierreNavDriveOwner<FragmentScreen, FragmentDialog>()

    private var containerId: Int = View.NO_ID
    private var restoredState: Bundle? = null

    private var render: AlcubierreRootNavRender? = null

    override val stateFlow: StateFlow<FragmentRootNavState> get() = delegate.stateFlow

    override val state: FragmentRootNavState get() = delegate.state

    /**
     * NavDriveOwner initialization. Should be called before any [dispatch] methods.
     * Should not be called before onCreate.
     *
     * @see NavDriveOwner.initialize
     */
    override fun initialize(
        reducer: NavReducer<AnyRootNavState>,
        initialState: FragmentRootNavState,
        extras: Map<KClass<*>, Any>
    ) {
        check(lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED))

        val render = AlcubierreRootNavRender(
            containerId = containerId,
            classLoader = requireContext().classLoader,
            fragmentManager = childFragmentManager,
            navDriveOwner = this,
            transactionModifier = extras.getAndCast() ?: EmptyModifier
        )
        this.render = render

        delegate.initialize(
            reducer = reducer,
            initialState = initialState,
        )
        restoredState?.let { state ->
            delegate.restoreState(state)
            render.restoreState(state)
        }
        restoredState = null

        render.renderFrom(delegate.stateFlow, lifecycle)
    }

    override fun dispatch(action: NavAction<FragmentScreen, FragmentDialog>) {
        delegate.dispatch(action)
    }

    override fun requestDismissDialog() {
        delegate.requestDismissDialog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this) { back() }
        parentFragmentManager.commit(allowStateLoss = true) {
            setPrimaryNavigationFragment(this@AlcubierreNavDriveFragment)
        }
    }

    override fun saveState(outState: Bundle) {
        throw UnsupportedOperationException("AlcubierreNavDriveFragment manages its own state.")
    }

    override fun restoreState(savedState: Bundle?) {
        throw UnsupportedOperationException("AlcubierreNavDriveFragment manages its own state.")
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CONTAINER_ID, containerId)
        render?.let { render ->
            delegate.saveState(outState)
            render.saveState(outState)
        }
    }

    private companion object {

        private const val KEY_CONTAINER_ID = "alcubierre_container"
    }
}
