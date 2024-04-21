package com.github.octaone.alcubierre.deeplink.test

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.github.octaone.alcubierre.screen.Screen
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import com.github.octaone.alcubierre.screen.extra.ParcelableExtras

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
