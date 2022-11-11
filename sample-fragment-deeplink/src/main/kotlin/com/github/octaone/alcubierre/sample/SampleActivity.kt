package com.github.octaone.alcubierre.sample

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.action.back
import com.github.octaone.alcubierre.host.AlcubierreNavDriveOwner
import com.github.octaone.alcubierre.reduce.AlcubierreDefaultNavReducer
import com.github.octaone.alcubierre.reducer.DeeplinkReducer
import com.github.octaone.alcubierre.render.AlcubierreRootNavRender
import com.github.octaone.alcubierre.render.modifier.EmptyModifier
import com.github.octaone.alcubierre.sample.databinding.ActivitySampleBinding
import com.github.octaone.alcubierre.sample.screen.SampleScreen
import com.github.octaone.alcubierre.state.singleRootState

class SampleActivity : AppCompatActivity() {

    private val navDriveOwner: AlcubierreNavDriveOwner get() = SampleApplication.from(this).navDriveOwner
    private val navDrive: NavDrive get() = SampleApplication.from(this).navDrive

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

        val reducer = DeeplinkReducer(AlcubierreDefaultNavReducer())

        val render = AlcubierreRootNavRender(
            containerId = R.id.container,
            classLoader = classLoader,
            fragmentManager = supportFragmentManager,
            transactionModifier = EmptyModifier
        )

        navDriveOwner.initialize(
            reducer = reducer,
            render = render,
            savedState = savedInstanceState,
            initialState = singleRootState {
                screen(SampleScreen(Counter.increment()))
            }
        )
    }

    override fun onResume() {
        super.onResume()
        navDriveOwner.onResume()
    }

    override fun onPause() {
        super.onPause()
        navDriveOwner.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navDriveOwner.saveState(outState)
    }
}
