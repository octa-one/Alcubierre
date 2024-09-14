package com.github.octaone.alcubierre.sample

import android.app.Application
import android.content.Context
import com.github.octaone.alcubierre.FragmentNavDrive
import com.github.octaone.alcubierre.FragmentNavDriveOwner
import com.github.octaone.alcubierre.owner.AlcubierreNavDriveOwner

class SampleApplication : Application() {

    lateinit var navDriveOwner: FragmentNavDriveOwner
    val navDrive: FragmentNavDrive get() = navDriveOwner

    override fun onCreate() {
        super.onCreate()
        navDriveOwner = AlcubierreNavDriveOwner()
    }

    companion object {

        fun from(context: Context): SampleApplication =
            context.applicationContext as SampleApplication
    }
}
