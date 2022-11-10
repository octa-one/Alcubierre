package com.github.octaone.alcubierre.codegen.test

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.github.octaone.alcubierre.screen.Screen

@SuppressLint("ParcelCreator")
open class TestScreen : Screen, Parcelable by ParcelableStub {

    override val screenId: String = ""
}

@SuppressLint("ParcelCreator")
private object ParcelableStub : Parcelable {
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) = Unit
}
