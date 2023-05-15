package com.github.octaone.alcubierre.render

import android.os.Bundle
import android.os.Parcelable

interface FragmentNavRender<T : Parcelable?> {

    fun render(state: T)

    fun saveState(outState: Bundle)

    fun restoreState(savedState: Bundle?)
}
