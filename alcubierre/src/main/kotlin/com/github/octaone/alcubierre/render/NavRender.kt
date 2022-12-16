package com.github.octaone.alcubierre.render

import android.os.Bundle
import android.os.Parcelable

interface NavRender<T : Parcelable?> {

    fun render(state: T)

    fun saveState(outState: Bundle)

    fun restoreState(bundle: Bundle)
}
