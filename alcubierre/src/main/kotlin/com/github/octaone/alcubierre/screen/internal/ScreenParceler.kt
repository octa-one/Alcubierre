package com.github.octaone.alcubierre.screen.internal

import android.annotation.SuppressLint
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Extras
import com.github.octaone.alcubierre.screen.Screen
import kotlinx.parcelize.Parceler

object ScreenParceler : Parceler<Screen> {

    @Suppress("DEPRECATION")
    @SuppressLint("ParcelClassLoader")
    override fun create(parcel: Parcel): Screen = createGeneric(parcel, Screen::class.java)

    override fun Screen.write(parcel: Parcel, flags: Int) {
        writeGeneric(parcel, flags)
    }
}

object DialogParceler : Parceler<Dialog> {

    @Suppress("DEPRECATION")
    @SuppressLint("ParcelClassLoader")
    override fun create(parcel: Parcel): Dialog = createGeneric(parcel, Dialog::class.java)

    override fun Dialog.write(parcel: Parcel, flags: Int) {
        writeGeneric(parcel, flags)
    }
}

@Suppress("DEPRECATION")
@SuppressLint("ParcelClassLoader")
private fun <T> createGeneric(parcel: Parcel, clazz: Class<T>): T where T : Parcelable, T : Extras {
    val screen = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        parcel.readParcelable(clazz.classLoader, clazz)!!
    } else {
        parcel.readParcelable(clazz.classLoader)!!
    }
    parcel.readBundle()?.let { extras ->
        screen.extras.putAll(extras)
    }
    return screen
}

private fun <T> T.writeGeneric(parcel: Parcel, flags: Int) where T : Parcelable, T : Extras {
    parcel.writeParcelable(this, flags)
    if (hasExtras()) parcel.writeBundle(extras) else parcel.writeBundle(null)
}
