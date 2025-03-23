package space.octaone.alcubierre.sample

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import space.octaone.alcubierre.FragmentNavDrive
import space.octaone.alcubierre.FragmentNavDriveOwner
import space.octaone.alcubierre.action.back
import space.octaone.alcubierre.action.selectStack
import space.octaone.alcubierre.base.reduce.builder.reducerLinkedListOf
import space.octaone.alcubierre.base.state.rootState
import space.octaone.alcubierre.reduce.BatchRootNavReducer
import space.octaone.alcubierre.reduce.DialogRootNavReducer
import space.octaone.alcubierre.reduce.ScreenRootNavReducer
import space.octaone.alcubierre.reduce.StackChangedListenerReducer
import space.octaone.alcubierre.render.AlcubierreRootNavRender
import space.octaone.alcubierre.render.modifier.EmptyModifier
import space.octaone.alcubierre.render.renderFrom
import space.octaone.alcubierre.sample.databinding.ActivitySampleBinding
import space.octaone.alcubierre.sample.screen.SampleScreen

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
                        if (navDrive.state.stackStates.containsKey(R.id.stack_2)) {
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

        val reducer = reducerLinkedListOf(
            StackChangedListenerReducer { _, to ->
                onItemSelectedListener.isEnabled = false
                binding.bottomNavigation.selectedItemId = to
                onItemSelectedListener.isEnabled = true

            },
            BatchRootNavReducer(),
            DialogRootNavReducer(),
            ScreenRootNavReducer()
        )

        navDriveOwner.initialize(
            reducer = reducer,
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

        navDriveOwner.restoreState(savedInstanceState)
        render.restoreState(savedInstanceState)

        render.renderFrom(navDriveOwner.stateFlow, lifecycle)

        binding.bottomNavigation.setOnItemSelectedListener(onItemSelectedListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navDriveOwner.saveState(outState)
        render.saveState(outState)
    }
}
