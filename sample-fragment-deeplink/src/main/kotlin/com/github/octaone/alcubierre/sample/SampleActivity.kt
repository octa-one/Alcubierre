package com.github.octaone.alcubierre.sample

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.github.octaone.alcubierre.FragmentNavDrive
import com.github.octaone.alcubierre.FragmentNavDriveOwner
import com.github.octaone.alcubierre.action.back
import com.github.octaone.alcubierre.condition.DefaultNavConditionFactory
import com.github.octaone.alcubierre.condition.reducer.ConditionReducer
import com.github.octaone.alcubierre.reduce.BatchRootNavReducer
import com.github.octaone.alcubierre.reduce.DialogRootNavReducer
import com.github.octaone.alcubierre.reduce.ScreenRootNavReducer
import com.github.octaone.alcubierre.reduce.builder.reducerLinkedListOf
import com.github.octaone.alcubierre.reducer.DeeplinkReducer
import com.github.octaone.alcubierre.render.AlcubierreRootNavRender
import com.github.octaone.alcubierre.render.modifier.EmptyModifier
import com.github.octaone.alcubierre.render.renderFrom
import com.github.octaone.alcubierre.sample.databinding.ActivitySampleBinding
import com.github.octaone.alcubierre.sample.screen.SampleScreen
import com.github.octaone.alcubierre.state.singleStackRootState

class SampleActivity : AppCompatActivity() {

    private val navDriveOwner: FragmentNavDriveOwner
        get() = SampleApplication.from(this).navDriveOwner
    private val navDrive: FragmentNavDrive
        get() = SampleApplication.from(this).navDrive

    private val render by lazy(LazyThreadSafetyMode.NONE) {
        AlcubierreRootNavRender(
            containerId = R.id.container,
            classLoader = classLoader,
            fragmentManager = supportFragmentManager,
            navDriveOwner = navDriveOwner,
            transactionModifier = EmptyModifier
        )
    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navDrive.back()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        navDriveOwner.initialize(
            reducer = reducerLinkedListOf(
                BatchRootNavReducer(),
                DeeplinkReducer(onResolveFailed = {}),
                ConditionReducer(DefaultNavConditionFactory()),
                DialogRootNavReducer(),
                ScreenRootNavReducer()
            ),
            initialState = singleStackRootState {
                screen(SampleScreen(Counter.increment()))
            }
        )

        navDriveOwner.restoreState(savedInstanceState)
        render.restoreState(savedInstanceState)

        render.renderFrom(navDriveOwner.stateFlow, lifecycle)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navDriveOwner.saveState(outState)
        render.saveState(outState)
    }
}
