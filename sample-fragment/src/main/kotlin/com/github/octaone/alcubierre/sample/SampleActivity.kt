package com.github.octaone.alcubierre.sample

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.action.back
import com.github.octaone.alcubierre.action.selectStack
import com.github.octaone.alcubierre.host.AlcubierreNavDriveOwner
import com.github.octaone.alcubierre.reduce.AlcubierreDefaultNavReducer
import com.github.octaone.alcubierre.reduce.addOnStackChangedListener
import com.github.octaone.alcubierre.render.AlcubierreRootNavRender
import com.github.octaone.alcubierre.render.modifier.EmptyModifier
import com.github.octaone.alcubierre.sample.databinding.ActivitySampleBinding
import com.github.octaone.alcubierre.sample.screen.SampleScreen
import com.github.octaone.alcubierre.state.rootState
import com.google.android.material.navigation.NavigationBarView

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

        val onItemSelectedListener = object : NavigationBarView.OnItemSelectedListener {

            var isEnabled = true

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (!isEnabled) return true

                return when (item.itemId) {
                    R.id.stack_0 -> {
                        navDrive.selectStack(R.id.stack_0)
                        true
                    }
                    R.id.stack_1 -> {
                        navDrive.selectStack(R.id.stack_1)
                        true
                    }
                    R.id.stack_2 -> {
                        if (navDrive.state.stacks.containsKey(R.id.stack_2)) {
                            navDrive.selectStack(R.id.stack_2)
                            true
                        } else {
                            Toast.makeText(this@SampleActivity, "Stack does not exist", Toast.LENGTH_SHORT).show()
                            false
                        }
                    }
                    else -> {
                        false
                    }
                }
            }
        }

        val reducer = AlcubierreDefaultNavReducer()
            .addOnStackChangedListener { _, to ->
                onItemSelectedListener.isEnabled = false
                binding.bottomNavigation.selectedItemId = to
                onItemSelectedListener.isEnabled = true
            }

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
            initialState = rootState {
                stack(R.id.stack_0) {
                    screen(SampleScreen(Counter.increment()))
                }
                stack(R.id.stack_1) {
                    screen(SampleScreen(Counter.increment()))
                    screen(SampleScreen(Counter.increment()))
                }
                stack(R.id.stack_2) {
                    screen(SampleScreen(Counter.increment()))
                }
            }
        )

        binding.bottomNavigation.setOnItemSelectedListener(onItemSelectedListener)
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
