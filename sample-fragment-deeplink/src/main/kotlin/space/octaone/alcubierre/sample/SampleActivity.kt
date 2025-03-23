/*
 *  Copyright 2022-2025. Alcubierre Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.octaone.alcubierre.sample

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import space.octaone.alcubierre.FragmentNavDrive
import space.octaone.alcubierre.FragmentNavDriveOwner
import space.octaone.alcubierre.action.back
import space.octaone.alcubierre.base.reduce.builder.reducerLinkedListOf
import space.octaone.alcubierre.base.state.singleStackRootState
import space.octaone.alcubierre.condition.DefaultNavConditionFactory
import space.octaone.alcubierre.condition.reducer.ConditionReducer
import space.octaone.alcubierre.deeplink.DefaultDeeplinkResolver
import space.octaone.alcubierre.reduce.BatchRootNavReducer
import space.octaone.alcubierre.reduce.DialogRootNavReducer
import space.octaone.alcubierre.reduce.ScreenRootNavReducer
import space.octaone.alcubierre.reducer.DeeplinkReducer
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

        navDriveOwner.initialize(
            reducer = reducerLinkedListOf(
                BatchRootNavReducer(),
                DeeplinkReducer(
                    resolver = DefaultDeeplinkResolver(DeeplinkRegistryCollector().registries),
                    onResolveFailed = {}
                ),
                ConditionReducer(DefaultNavConditionFactory(classLoader)),
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
