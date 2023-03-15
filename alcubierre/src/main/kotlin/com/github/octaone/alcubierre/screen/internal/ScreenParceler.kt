package com.github.octaone.alcubierre.screen.internal

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.ParcelCompat
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.extra.ParcelableExtras
import kotlinx.parcelize.Parceler

object ScreenParceler : Parceler<Screen> {

    override fun create(parcel: Parcel): Screen = createGeneric(parcel, Screen::class.java)

    override fun Screen.write(parcel: Parcel, flags: Int) {
        writeGeneric(parcel, flags)
    }
}

object DialogParceler : Parceler<Dialog> {

    override fun create(parcel: Parcel): Dialog = createGeneric(parcel, Dialog::class.java)

    override fun Dialog.write(parcel: Parcel, flags: Int) {
        writeGeneric(parcel, flags)
    }
}

@SuppressLint("ParcelClassLoader")
private fun <T> createGeneric(parcel: Parcel, clazz: Class<T>): T where T : Parcelable, T : ParcelableExtras {
    val screen = ParcelCompat.readParcelable(parcel, clazz.classLoader, clazz)!!
    parcel.readBundle()?.let { extras -> screen.restoreExtras(extras) }
    return screen
}

private fun <T> T.writeGeneric(parcel: Parcel, flags: Int) where T : Parcelable, T : ParcelableExtras {
    parcel.writeParcelable(this, flags)
    parcel.writeBundle(saveExtras())
}
