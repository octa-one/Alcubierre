package com.github.octaone.alcubierre.screen

import android.os.Bundle

interface Extras {

    val extras: Bundle

    fun hasExtras(): Boolean
}

class ExtrasLazyImpl : Extras {

    private val extrasLazy = lazy { Bundle() }

    override val extras: Bundle by extrasLazy

    override fun hasExtras(): Boolean = extrasLazy.isInitialized()
}
