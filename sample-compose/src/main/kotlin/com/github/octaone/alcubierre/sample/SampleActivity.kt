package com.github.octaone.alcubierre.sample

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.github.octaone.alcubierre.LocalNavDrive
import com.github.octaone.alcubierre.action.back
import com.github.octaone.alcubierre.owner.AlcubierreNavDriveOwner
import com.github.octaone.alcubierre.reduce.AlcubierreDefaultNavReducer
import com.github.octaone.alcubierre.render.AlcubierreRender
import com.github.octaone.alcubierre.render.NavDriveOwnerSaver
import com.github.octaone.alcubierre.sample.screen.SampleScreen
import com.github.octaone.alcubierre.screen.ComposeDialog
import com.github.octaone.alcubierre.screen.ComposeScreen
import com.github.octaone.alcubierre.state.singleRootState

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navDriveOwner = remember {
                AlcubierreNavDriveOwner<ComposeScreen, ComposeDialog>().also { owner ->
                    owner.initialize(
                        AlcubierreDefaultNavReducer(),
                        singleRootState { screen(SampleScreen(Counter.increment())) }
                    )
                }
            }

            CompositionLocalProvider(
                LocalNavDrive provides navDriveOwner
            ) {
                NavDriveOwnerSaver(navDriveOwner)
                BackHandler { navDriveOwner.back() }
                AlcubierreRender(navDriveOwner)
            }
        }
    }
}
