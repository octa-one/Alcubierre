package com.github.octaone.alcubierre.codegen.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.github.octaone.alcubierre.screen.Extras
import com.github.octaone.alcubierre.screen.Screen

@SuppressLint("ParcelCreator")
open class TestScreen : Screen, Parcelable by ParcelableStub, Extras by ExtrasStub {

    override val screenId: String = ""
}

private object ExtrasStub : Extras {

    override val extras: Bundle get() = Bundle.EMPTY

    override fun hasExtras(): Boolean = false
}

@SuppressLint("ParcelCreator")
private object ParcelableStub : Parcelable {
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) = Unit
}
