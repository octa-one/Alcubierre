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
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import space.octaone.alcubierre.LocalNavDrive
import space.octaone.alcubierre.action.back
import space.octaone.alcubierre.action.selectStack
import space.octaone.alcubierre.base.reduce.builder.reducerLinkedListOf
import space.octaone.alcubierre.base.state.rootState
import space.octaone.alcubierre.owner.AlcubierreNavDriveOwner
import space.octaone.alcubierre.reduce.BatchRootNavReducer
import space.octaone.alcubierre.reduce.DialogRootNavReducer
import space.octaone.alcubierre.reduce.ScreenRootNavReducer
import space.octaone.alcubierre.render.AlcubierreAnimatedRender
import space.octaone.alcubierre.render.NavDriveOwnerSaver
import space.octaone.alcubierre.sample.screen.SampleScreen
import space.octaone.alcubierre.screen.ComposeDialog
import space.octaone.alcubierre.screen.ComposeScreen

@AndroidEntryPoint
class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navDriveOwner = remember {
                AlcubierreNavDriveOwner<ComposeScreen, ComposeDialog>().also { owner ->
                    owner.initialize(
                        reducer = reducerLinkedListOf(
                            BatchRootNavReducer(),
                            DialogRootNavReducer(),
                            ScreenRootNavReducer()
                        ),
                        initialState = rootState {
                            stack(Tab.TAB_0.id) {
                                screen(SampleScreen(Counter.increment()))
                            }
                            stack(Tab.TAB_1.id) {
                                screen(SampleScreen(Counter.increment()))
                                screen(SampleScreen(Counter.increment()))
                            }
                            stack(Tab.TAB_2.id) {
                                screen(SampleScreen(Counter.increment()))
                            }
                        }
                    )
                }
            }

            Scaffold(
                bottomBar = {
                    val currentStackId by produceState(-1) {
                        navDriveOwner.stateFlow.collect { state ->
                            value = if (state.stackStates.size > 1) state.currentStackId else -1
                        }
                    }
                    if (currentStackId != -1) {
                        BottomAppBar {
                            Tab.entries.forEach { tab ->
                                key(tab.id) {
                                    val isSelected = currentStackId == tab.id
                                    NavigationBarItem(
                                        icon = {
                                            Icon(if (isSelected) tab.selectedIcon else tab.icon, null)
                                        },
                                        label = null,
                                        selected = isSelected,
                                        onClick = { navDriveOwner.selectStack(tab.id) },
                                    )
                                }
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    CompositionLocalProvider(
                        LocalNavDrive provides navDriveOwner
                    ) {
                        NavDriveOwnerSaver(navDriveOwner)
                        BackHandler { navDriveOwner.back() }
                        AlcubierreAnimatedRender(
                            navDriveOwner = navDriveOwner,
                            addTransition = {
                                slideInVertically { height -> height } + fadeIn() togetherWith
                                        slideOutVertically { height -> -height } + fadeOut()
                            },
                            removeTransition = {
                                slideInVertically { height -> -height } + fadeIn() togetherWith
                                        slideOutVertically { height -> height } + fadeOut()
                            }
                        )
                    }
                }
            }
        }
    }
}
