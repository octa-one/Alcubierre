package space.octaone.alcubierre.sample.screen

import kotlinx.parcelize.Parcelize
import space.octaone.alcubierre.sample.fragment.SampleDialogFragment
import space.octaone.alcubierre.screen.FragmentDialog

@Parcelize
data class SampleDialog(
    val someId: Int,
    override val priority: Int
) : FragmentDialog(SampleDialogFragment::class)
