package com.github.octaone.alcubierre.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RootSavedState(
    val state: RootNavState,
    val rendered: Map<Int, StackNavState>
) : Parcelable
