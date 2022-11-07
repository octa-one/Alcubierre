package com.github.octaone.alcubierre.sample.screen

import com.github.octaone.alcubierre.sample.fragment.SampleFragment
import com.github.octaone.alcubierre.screen.FragmentScreen
import kotlinx.parcelize.Parcelize

@Parcelize
data class SampleScreen(
    val someId: Int
) : FragmentScreen(SampleFragment::class)
