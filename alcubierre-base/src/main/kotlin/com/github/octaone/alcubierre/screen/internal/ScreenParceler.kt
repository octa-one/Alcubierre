@file:OptIn(AlcubierreInternalApi::class)

package com.github.octaone.alcubierre.screen.internal

import android.os.Parcel
import android.os.Parcelable
import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi
import com.github.octaone.alcubierre.base.util.readBooleanCompat
import com.github.octaone.alcubierre.base.util.readParcelableCompat
import com.github.octaone.alcubierre.base.util.writeBooleanCompat
import com.github.octaone.alcubierre.screen.Dialog
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import kotlinx.parcelize.Parceler

internal object ScreenParceler : Parceler<Screen> {

    override fun create(parcel: Parcel): Screen =
        readGeneric(parcel, Screen::class.java).apply {
            screenId = parcel.readString()!!
        }

    override fun Screen.write(parcel: Parcel, flags: Int) {
        writeGeneric(parcel, flags)
        parcel.writeString(screenId)
    }
}

internal object DialogParceler : Parceler<Dialog> {

    override fun create(parcel: Parcel): Dialog =
        readGeneric(parcel, Dialog::class.java).apply {
            dialogId = parcel.readString()!!
            isShowing = parcel.readBooleanCompat()
        }

    override fun Dialog.write(parcel: Parcel, flags: Int) {
        writeGeneric(parcel, flags)
        parcel.writeString(dialogId)
        parcel.writeBooleanCompat(isShowing)
    }
}

private fun <T> readGeneric(parcel: Parcel, cl: Class<T>): T where T : Parcelable, T : ExtrasContainer {
    val screen = parcel.readParcelableCompat<T>(cl.classLoader, cl)!!
    parcel.readBundle(cl.classLoader)?.let { extras -> screen.extras.restore(extras) }
    return screen
}

private fun <T> T.writeGeneric(parcel: Parcel, flags: Int) where T : Parcelable, T : ExtrasContainer {
    parcel.writeParcelable(this, flags)
    parcel.writeBundle(if (hasExtras()) extras.save() else null)
}
