package com.github.octaone.alcubierre.screen.internal

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.ParcelCompat
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import kotlinx.parcelize.Parceler

object ScreenParceler : Parceler<Screen> {

    override fun create(parcel: Parcel): Screen =
        createGeneric(parcel, Screen::class.java)
            .apply { screenId = parcel.readString()!! }

    override fun Screen.write(parcel: Parcel, flags: Int) {
        writeGeneric(parcel, flags)
        parcel.writeString(screenId)
    }
}

object DialogParceler : Parceler<Dialog> {

    override fun create(parcel: Parcel): Dialog =
        createGeneric(parcel, Dialog::class.java)
            .apply { dialogId = parcel.readString()!! }

    override fun Dialog.write(parcel: Parcel, flags: Int) {
        writeGeneric(parcel, flags)
        parcel.writeString(dialogId)
    }
}

@SuppressLint("ParcelClassLoader")
private fun <T> createGeneric(parcel: Parcel, clazz: Class<T>): T where T : Parcelable, T : ExtrasContainer {
    val screen = ParcelCompat.readParcelable(parcel, clazz.classLoader, clazz)!!
    parcel.readBundle()?.let { extras -> screen.extras.restore(extras) }
    return screen
}

private fun <T> T.writeGeneric(parcel: Parcel, flags: Int) where T : Parcelable, T : ExtrasContainer {
    parcel.writeParcelable(this, flags)
    parcel.writeBundle(if (hasExtras()) extras.save() else null)
}
