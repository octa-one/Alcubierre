package com.github.octaone.alcubierre.render

import android.os.Parcelable

interface Render<T : Parcelable?> {

    val currentState: T

    fun render(state: T)

    fun restoreState(state: T)
}
