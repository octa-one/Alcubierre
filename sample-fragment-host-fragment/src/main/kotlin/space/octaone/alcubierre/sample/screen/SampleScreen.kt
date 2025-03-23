package space.octaone.alcubierre.sample.screen

import kotlinx.parcelize.Parcelize
import space.octaone.alcubierre.sample.fragment.SampleFragment
import space.octaone.alcubierre.screen.FragmentScreen

@Parcelize
data class SampleScreen(
    val someId: Int
) : FragmentScreen(SampleFragment::class)
