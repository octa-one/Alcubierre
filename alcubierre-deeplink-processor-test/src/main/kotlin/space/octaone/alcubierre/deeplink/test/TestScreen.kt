package space.octaone.alcubierre.deeplink.test

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import space.octaone.alcubierre.base.screen.Screen
import space.octaone.alcubierre.base.screen.extra.ExtrasContainer
import space.octaone.alcubierre.base.screen.extra.ParcelableExtras

@SuppressLint("ParcelCreator", "NonDataScreenClassRule")
open class TestScreen : Screen(), Parcelable by ParcelableStub, ExtrasContainer by ExtrasContainerStub

private object ExtrasContainerStub : ExtrasContainer {

    override val extras: ParcelableExtras get() = throw UnsupportedOperationException()

    override fun hasExtras(): Boolean = false
}

@SuppressLint("ParcelCreator")
private object ParcelableStub : Parcelable {
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) = Unit
}
