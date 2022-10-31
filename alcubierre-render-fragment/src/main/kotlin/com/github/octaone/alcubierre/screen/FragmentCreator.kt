package com.github.octaone.alcubierre.screen

import androidx.fragment.app.Fragment

/**
 * Расширение для [FragmentScreen], когда необходимо вручную создать фрагмент, например фрагмент из библиотеки.
 */
interface FragmentCreator {

    fun create(): Fragment
}
