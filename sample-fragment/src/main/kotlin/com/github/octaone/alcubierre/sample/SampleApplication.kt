package com.github.octaone.alcubierre.sample

import android.app.Application
import android.content.Context
import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.owner.AlcubierreNavDriveOwner

class SampleApplication : Application() {

    lateinit var navDriveOwner: AlcubierreNavDriveOwner
    val navDrive: NavDrive get() = navDriveOwner

    override fun onCreate() {
        super.onCreate()
        navDriveOwner = AlcubierreNavDriveOwner()
    }

    companion object {

        fun from(context: Context): SampleApplication =
            context.applicationContext as SampleApplication
    }
}