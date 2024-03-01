package com.github.octaone.alcubierre.sample

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
import com.github.octaone.alcubierre.LocalNavDrive
import com.github.octaone.alcubierre.action.back
import com.github.octaone.alcubierre.action.selectStack
import com.github.octaone.alcubierre.owner.AlcubierreNavDriveOwner
import com.github.octaone.alcubierre.reduce.AlcubierreDefaultNavReducer
import com.github.octaone.alcubierre.render.AlcubierreRender
import com.github.octaone.alcubierre.render.NavDriveOwnerSaver
import com.github.octaone.alcubierre.sample.screen.SampleScreen
import com.github.octaone.alcubierre.screen.ComposeDialog
import com.github.octaone.alcubierre.screen.ComposeScreen
import com.github.octaone.alcubierre.state.rootState

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navDriveOwner = remember {
                AlcubierreNavDriveOwner<ComposeScreen, ComposeDialog>().also { owner ->
                    owner.initialize(
                        reducer = AlcubierreDefaultNavReducer(),
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
                                        onClick = remember {{ navDriveOwner.selectStack(tab.id) }},
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
                        AlcubierreRender(
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
