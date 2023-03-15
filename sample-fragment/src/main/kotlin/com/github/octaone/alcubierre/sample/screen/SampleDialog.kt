package com.github.octaone.alcubierre.sample.screen

import com.github.octaone.alcubierre.sample.fragment.SampleDialogFragment
import com.github.octaone.alcubierre.screen.FragmentDialog
import kotlinx.parcelize.Parcelize

@Parcelize
data class SampleDialog(
    val someId: Int,
    override val priority: Int
) : FragmentDialog(SampleDialogFragment::class)
